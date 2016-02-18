package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.common.collect.Lists;

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
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils;
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.DatePickerDialogFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserMultiSelectDialogFragment;
import app.android.ttp.mikazuki.yoshinani.viewModel.PaymentViewModel;
import app.android.ttp.mikazuki.yoshinani.viewModel.UserViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.Observable;

public class PostPaymentFragment extends PostFragment {

    private static final int USER_SELECT = 111;
    private static final int DATE_DIALOG_ID = 999;
    private final String TAG = PostPaymentFragment.class.getSimpleName();
    @Bind(R.id.amount_input_layout)
    TextInputLayout mAmountTextInputLayout;
    @Bind(R.id.participants)
    Button mParticipantsButton;

    FragmentPostPaymentBinding binding;
    InterstitialAd mInterstitialAd;
    private UserViewModel mUserViewModel;
    private PaymentViewModel mPaymentViewModel;
    private PaymentModel mPaymentModel;
    private GroupModel mGroupModel;
    private List<UserModel> mAllUserModels;

    private ArrayList<Integer> mParticipantsIds;

    public PostPaymentFragment() {
    }

    public static PostPaymentFragment newInstance() {
        PostPaymentFragment fragment = new PostPaymentFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_payment, container, false);
        ButterKnife.bind(this, view);

        mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));

        mUserViewModel = new UserViewModel(getActivity().getApplicationContext());
        mPaymentViewModel = new PaymentViewModel(getActivity().getApplicationContext());

        // TextInputLayoutのhintアニメーションを削除
        ViewUtils.disableTextInputLayoutHint(view, R.id.amount_input_layout);
        ViewUtils.disableTextInputLayoutHint(view, R.id.event_input_layout);
        ViewUtils.disableTextInputLayoutHint(view, R.id.description_input_layout);
        mAmountTextInputLayout.getEditText().setOnFocusChangeListener((v, hasFocus) -> {
            Log.d("!!!", "focus change!! " + hasFocus);
            if (hasFocus) {
                ((EditText) v).setSelection(((EditText) v).getText().length());
            }
        });

        mUserViewModel.getAll(mGroupModel.getId());

        // 全画面広告の準備
        mInterstitialAd = new InterstitialAd(getActivity().getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        requestNewInterstitial();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
//                getActivity().onBackPressed();
            }
        });

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
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("YOUR_DEVICE_HASH")
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
        mPaymentViewModel.create(mPaymentModel);
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    public void onEvent(DateSetEvent event) {
        mPaymentModel.setDate(event.getDate());
    }

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
        mParticipantsButton.setText(names);
    }

    public void onEvent(FetchListDataEvent<UserModel> event) {
        mAllUserModels = event.getListData();
        mParticipantsButton.setEnabled(true);
    }

    public void onEvent(FetchDataEvent<PaymentModel> event) {
        // 初期化
        mPaymentModel.reset();

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            getActivity().onBackPressed();
        }
    }

}
