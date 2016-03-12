package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.BuildConfig;
import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.ErrorEvent;
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.services.AuthService;
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils;
import app.android.ttp.mikazuki.yoshinani.view.activity.MainActivity;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class SignInFragment extends Fragment {

    @Bind(R.id.account)
    EditText mAccount;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.login_btn)
    Button mLogin;

    private AuthService mAuthService;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        ButterKnife.bind(this, view);
        mAuthService = new AuthService(getActivity().getApplicationContext());

        // バリデーション
        Observable<Boolean> isAccountCompleted = RxTextView.textChanges(mAccount).map(StringUtils::isNotEmpty);
        Observable<Boolean> isPasswordCompleted = RxTextView.textChanges(mPassword).map(StringUtils::isNotEmpty);
        Observable<Boolean> isValidAll = Observable.combineLatest(isAccountCompleted, isPasswordCompleted, (a, p) -> a && p);
        compositeSubscription.add(isValidAll.subscribe(isValid -> mLogin.setEnabled(isValid)));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        compositeSubscription.unsubscribe();
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * Listener methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.login_btn)
    public void onClickButton(View view) {
        if (BuildConfig.DEBUG && (mAccount.getText() == null || mAccount.getText().length() <= 0)) {
            // デバッグ用
            mAuthService.signIn("haijima", "password1!");
        } else {
            mAuthService.signIn(mAccount.getText().toString(), mPassword.getText().toString());
            ViewUtils.hideKeyboard(getActivity());
        }
    }

    @OnClick(R.id.go_to_sign_up)
    public void goToSignUp(View view) {
        EventBus.getDefault().post(new FragmentTransitionEvent(new SignUpFragment()));
    }

    @OnClick(R.id.forget)
    public void forgetAccoutnOrPassword(View view) {
        final View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_forget_account_or_password, null);
        new AlertDialog.Builder(getActivity())
                .setTitle("アカウント再設定")
                .setView(dialogView)
                .setPositiveButton("送信", (dialog, id) ->
                        mAuthService
                                .resetPassword(
                                        ((EditText) dialogView.findViewById(R.id.token)).getText().toString(),
                                        ((EditText) dialogView.findViewById(R.id.new_password)).getText().toString(),
                                        ((EditText) dialogView.findViewById(R.id.new_password_confirm)).getText().toString())
                                .subscribe(response -> {
                                    if (response.isSuccess()) {
                                        PreferenceUtil.putUserData(getActivity().getApplicationContext(), response.body());
                                        EventBus.getDefault().post(new ActivityTransitionEvent(MainActivity.class, false));
                                    } else {
                                        EventBus.getDefault().post(new ErrorEvent("アカウント再設定失敗", ApiUtil.getApiError(response).getMessage()));
                                    }
                                }))
                .setNeutralButton("認証キー発行", (dialog1, which) -> {
                    final View nextDialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_generate_reset_token, null);
                    new AlertDialog.Builder(getActivity())
                            .setTitle("認証キー発行")
                            .setView(nextDialogView)
                            .setPositiveButton("送信", (dialog, id) ->
                                    mAuthService
                                            .forgetPassword(((EditText) nextDialogView.findViewById(R.id.email)).getText().toString())
                                            .subscribe(response -> {
                                                if (response.isSuccess()) {
                                                    EventBus.getDefault().post(new ShowSnackbarEvent(response.body().getMessage()));
                                                } else {
                                                    EventBus.getDefault().post(new ErrorEvent("確認用メール送信失敗", ApiUtil.getApiError(response).getMessage()));
                                                }
                                            })
                            ).create().show();
                }).create().show();
    }

}
