package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.jakewharton.rxbinding.widget.RxTextView;

import org.apache.commons.lang3.StringUtils;
import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.services.AuthService;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class SignUpFragment extends Fragment {

    @Bind(R.id.account)
    EditText mAccount;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.password)
    EditText mPassword;
    @Bind(R.id.password_confirm)
    EditText mPasswordConfirm;

    @Bind(R.id.register_btn)
    Button mRegister;

    private AuthService mAuthService;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);

        mAuthService = new AuthService(getActivity().getApplicationContext());

        // バリデーション
        Observable<Boolean> isAccountCompleted = RxTextView.textChanges(mAccount).map(StringUtils::isNotEmpty);
        Observable<Boolean> isEmailCompleted = RxTextView.textChanges(mEmail).map(StringUtils::isNotEmpty);
        Observable<Boolean> isPasswordCompleted = RxTextView.textChanges(mPassword).map(StringUtils::isNotEmpty);
        Observable<Boolean> isPasswordConfirmCompleted = RxTextView.textChanges(mPasswordConfirm).map(StringUtils::isNotEmpty);
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
        ButterKnife.unbind(this);
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