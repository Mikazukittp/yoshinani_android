package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.viewModel.AuthViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @author haijimakazuki
 */
public class SignUpFragment extends Fragment {

    @Bind(R.id.account)
    EditText mAccount;
    @Bind(R.id.username)
    EditText mUsername;
    @Bind(R.id.email)
    EditText mEmail;
    @Bind(R.id.password)
    EditText mPassword;

    private AuthViewModel mAuthViewModel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        ButterKnife.bind(this, view);

        mAuthViewModel = new AuthViewModel(getActivity().getApplicationContext());
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
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * Listener methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.register_btn)
    public void onClickButton(View view) {
        mAuthViewModel.signUp(
                mAccount.getText().toString(),
                mUsername.getText().toString(),
                mEmail.getText().toString(),
                mPassword.getText().toString()
        );
    }

    @OnClick(R.id.go_to_sign_in)
    public void goToSignUp(View view) {
        EventBus.getDefault().post(new FragmentTransitionEvent(new SignInFragment()));
    }

}
