package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.FragmentPostRepaymentBinding;
import app.android.ttp.mikazuki.yoshinani.event.DateSetEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.UserSelectEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.services.PaymentService;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.DatePickerDialogFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserSelectDialogFragment;
import app.android.ttp.mikazuki.yoshinani.viewModel.PostRepaymentViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class PostRepaymentFragment extends PostFragment {

    private static final int USER_SELECT = 111;
    private static final int DATE_DIALOG_ID = 999;
    private final String TAG = PostPaymentFragment.class.getSimpleName();
    @BindView(R.id.amount)
    EditText mAmount;
    @BindView(R.id.participants)
    Button mParticipantsBtn;
    @BindView(R.id.post)
    Button mPost;
    private Unbinder mUnbinder;

    FragmentPostRepaymentBinding binding;
    private UserService mUserService;
    private PaymentService mPaymentService;

    private PostRepaymentViewModel mViewModel;
    private GroupModel mGroupModel;
    private List<UserModel> mAllUserModels;
    private int mParticipantId;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public PostRepaymentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_repayment, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));

        mUserService = new UserService(getActivity().getApplicationContext());
        mPaymentService = new PaymentService(getActivity().getApplicationContext());

        // TextInputLayoutのhintアニメーションを削除
        ViewUtils.disableTextInputLayoutHint(view, R.id.amount_input_layout);

        mUserService.getAll(mGroupModel.getId());

        // バリデーション
        Observable<Boolean> isAmountCompleted = RxTextView.textChanges(mAmount).map(str -> !TextUtils.isEmpty(str));
        Observable<Boolean> isParticipantsSelected = RxTextView.textChanges(mParticipantsBtn).map(str -> !"返済相手選択".equals(str.toString()));
        Observable<Boolean> isValidAll = Observable.combineLatest(isAmountCompleted, isParticipantsSelected, (a, p) -> a && p);
        compositeSubscription.add(isValidAll.subscribe(isValid -> mPost.setEnabled(isValid)));

        return view;
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentPostRepaymentBinding.bind(view);

        mViewModel = new PostRepaymentViewModel(getActivity().getApplicationContext(), mGroupModel.getId());
        binding.setViewModel(mViewModel);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.date)
    public void onDateSelect(View v) {
        DialogFragment dialogFragment = new DatePickerDialogFragment();
        dialogFragment.setTargetFragment(PostRepaymentFragment.this, DATE_DIALOG_ID);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", mViewModel.getDate());
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.participants)
    public void onUserSelect(View v) {
        DialogFragment dialogFragment = new UserSelectDialogFragment();
        Bundle bundle = new Bundle();
        CharSequence[] users = new CharSequence[mAllUserModels.size()];
        for (int i = 0; i < users.length; i++) {
            users[i] = mAllUserModels.get(i).getDisplayName();
        }
        bundle.putCharSequenceArray("users", users);
        bundle.putInt("selected", mViewModel.getParticipantsId());
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(PostRepaymentFragment.this, USER_SELECT);
        dialogFragment.show(getFragmentManager(), "userSelect");
    }

    @OnClick(R.id.post)
    public void onPostPayment(View v) {
        int uid = Integer.parseInt(PreferenceUtil.getUid(getActivity().getApplicationContext()));
        UserModel me = new UserModel();
        me.setId(uid);
        mPaymentService.create(mViewModel.getModel());
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(DateSetEvent event) {
        mViewModel.setDate(event.getDate());
    }

    @Subscribe
    public void onEvent(UserSelectEvent event) {
        mViewModel.setParticipants(event.getValue());
    }

    @Subscribe
    public void onEvent(FetchListDataEvent<UserModel> event) {
        mAllUserModels = event.getListData();
        mViewModel.setAllUsers(mAllUserModels);
    }

    @Subscribe
    public void onEvent(FetchDataEvent<PaymentModel> event) {
        // 初期化
        mViewModel.reset();
        getActivity().onBackPressed();
    }

}
