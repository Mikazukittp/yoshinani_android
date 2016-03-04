package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.common.collect.Lists;
import com.jakewharton.rxbinding.widget.RxTextView;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.FragmentPostPaymentBinding;
import app.android.ttp.mikazuki.yoshinani.event.DateSetEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.UserMultiSelectEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.services.PaymentService;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils;
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.DatePickerDialogFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserMultiSelectDialogFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

public class PostPaymentFragment extends PostFragment {

    private static final int USER_SELECT = 111;
    private static final int DATE_DIALOG_ID = 999;
    @Bind(R.id.amount)
    EditText mAmount;
    @Bind(R.id.event)
    EditText mEvent;
    @Bind(R.id.description)
    EditText mDescription;
    @Bind(R.id.date)
    Button mDateBtn;
    @Bind(R.id.participants)
    Button mParticipantsBtn;
    @Bind(R.id.post)
    Button mPost;

    FragmentPostPaymentBinding binding;
    InterstitialAd mInterstitialAd;
    private UserService mUserService;
    private PaymentService mPaymentService;
    private PaymentModel mPaymentModel;
    private GroupModel mGroupModel;
    private List<UserModel> mAllUserModels;

    private ArrayList<Integer> mParticipantsIds;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public PostPaymentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_payment, container, false);
        ButterKnife.bind(this, view);

        mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));

        mUserService = new UserService(getActivity().getApplicationContext());
        mPaymentService = new PaymentService(getActivity().getApplicationContext());

        // TextInputLayoutのhintアニメーションを削除
        ViewUtils.disableTextInputLayoutHint(view, R.id.amount_input_layout);
        ViewUtils.disableTextInputLayoutHint(view, R.id.event_input_layout);
        ViewUtils.disableTextInputLayoutHint(view, R.id.description_input_layout);

        mUserService.getAll(mGroupModel.getId());

        // バリデーション
        Observable<Boolean> isAmountCompleted = RxTextView.textChanges(mAmount).map(StringUtils::isNotEmpty);
        Observable<Boolean> isEventCompleted = RxTextView.textChanges(mEvent).map(StringUtils::isNotEmpty);
        Observable<Boolean> isDescriptionCompleted = RxTextView.textChanges(mDescription).map(StringUtils::isNotEmpty);
        Observable<Boolean> isValidAll = Observable.combineLatest(isAmountCompleted, isEventCompleted, isDescriptionCompleted, (a, e, d) -> a && e && d);
        compositeSubscription.add(isValidAll.subscribe(isValid -> mPost.setEnabled(isValid)));

        // 全画面広告の準備
        requestNewInterstitial();

        return view;
    }

    @Override
    public void onViewCreated(View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = FragmentPostPaymentBinding.bind(view);

        mPaymentModel = new PaymentModel();
        mPaymentModel.setDate(ModelUtils.getToday());
        mPaymentModel.setGroupId(mGroupModel.getId());
        mPaymentModel.setIsRepayment(false);
        binding.setPayment(mPaymentModel);
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
        ButterKnife.unbind(this);
        compositeSubscription.unsubscribe();
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    private void requestNewInterstitial() {
        if (mInterstitialAd == null) {
            mInterstitialAd = new InterstitialAd(getActivity().getApplicationContext());
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    requestNewInterstitial();
                    getActivity().onBackPressed();
                }
            });
        }
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device_id))
                .build();
        mInterstitialAd.loadAd(adRequest);
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.date)
    public void onDateSelect(View v) {
        DialogFragment dialogFragment = new DatePickerDialogFragment();
        dialogFragment.setTargetFragment(PostPaymentFragment.this, DATE_DIALOG_ID);
        Bundle bundle = new Bundle();
        bundle.putSerializable("date", mPaymentModel.getDate());
        dialogFragment.setArguments(bundle);
        dialogFragment.show(getFragmentManager(), "datePicker");
    }

    @OnClick(R.id.participants)
    public void onUserSelect(View v) {
        DialogFragment dialogFragment = new UserMultiSelectDialogFragment();
        Bundle bundle = new Bundle();
        CharSequence[] users = new CharSequence[mAllUserModels.size()];
        for (int i = 0; i < users.length; i++) {
            users[i] = mAllUserModels.get(i).getUsername();
        }
        bundle.putCharSequenceArray("users", users);
        bundle.putIntegerArrayList("selected", mParticipantsIds);
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(PostPaymentFragment.this, USER_SELECT);
        dialogFragment.show(getFragmentManager(), "userSelect");
    }

    @OnClick(R.id.post)
    public void onPostPayment(View v) {
        int uid = Integer.parseInt(PreferenceUtil.getUid(getActivity().getApplicationContext()));
        UserModel me = new UserModel();
        me.setId(uid);
        List<UserModel> participants = Lists.newArrayList();
        for (int i = 0; i < mParticipantsIds.size(); i++) {
            participants.add(mAllUserModels.get(mParticipantsIds.get(i)));
        }
        mPaymentModel.setId(-1);
        mPaymentModel.setPaidUser(me);
        mPaymentModel.setParticipants(participants);
        mPaymentService.create(mPaymentModel);
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(DateSetEvent event) {
        mPaymentModel.setDate(event.getDate());
    }

    @Subscribe
    public void onEvent(UserMultiSelectEvent event) {
        mParticipantsIds = event.getValue();
        String names = (String) Observable.from(mParticipantsIds)
                .limit(3)
                .map(id -> mAllUserModels.get(id).getUsername())
                .reduce(null, (ns, n) -> ns == null ? n : ns + "," + n)
                .toBlocking().firstOrDefault("");
        if (mParticipantsIds.size() > 3) {
            names += " 他" + (mParticipantsIds.size() - 3) + "名";
        }
        mParticipantsBtn.setText(names);
    }

    @Subscribe
    public void onEvent(FetchListDataEvent<UserModel> event) {
        mAllUserModels = event.getListData();
        mParticipantsBtn.setEnabled(true);
    }

    @Subscribe
    public void onEvent(FetchDataEvent<PaymentModel> event) {
        mPaymentModel.reset();
        if (mInterstitialAd.isLoaded() && Math.random() < 0.2) {
            mInterstitialAd.show();
        } else {
            getActivity().onBackPressed();
        }
    }

}
