package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.services.AuthService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class SignUpFragment extends Fragment {

    @BindView(R.id.account)
    EditText mAccount;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.password_confirm)
    EditText mPasswordConfirm;

    @BindView(R.id.register_btn)
    Button mRegister;
    private Unbinder mUnbinder;

    private AuthService mAuthService;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        mAuthService = new AuthService(getActivity().getApplicationContext());

        // バリデーション
        Observable<Boolean> isAccountCompleted = RxTextView.textChanges(mAccount).map(str -> !TextUtils.isEmpty(str));
        Observable<Boolean> isEmailCompleted = RxTextView.textChanges(mEmail).map(str -> !TextUtils.isEmpty(str));
        Observable<Boolean> isPasswordCompleted = RxTextView.textChanges(mPassword).map(str -> !TextUtils.isEmpty(str));
        Observable<Boolean> isPasswordConfirmCompleted = RxTextView.textChanges(mPasswordConfirm).map(str -> !TextUtils.isEmpty(str));
        Observable<Boolean> isValidAll = Observable.combineLatest(isAccountCompleted, isEmailCompleted, isPasswordCompleted, isPasswordConfirmCompleted, (a, e, p, pc) -> a && e && p && pc);
        compositeSubscription.add(isValidAll.subscribe(isValid -> mRegister.setEnabled(isValid)));

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
        mUnbinder.unbind();
        compositeSubscription.unsubscribe();
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * Listener methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.register_btn)
    public void onClickButton(View view) {
        mAuthService.signUp(mAccount.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @OnClick(R.id.go_to_sign_in)
    public void goToSignUp(View view) {
        EventBus.getDefault().post(new FragmentTransitionEvent(new SignInFragment()));
    }

}
