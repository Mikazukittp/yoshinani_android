package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import javax.inject.Inject;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";
    @Inject
    public UserRepository mUserRepository;
    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        getAppComponent().inject(this);
//        mUserRepository = new RetrofitUserRepository(this);
    }

    private void signIn(String email, String password) {

        mUserRepository.signIn(email, password, new BaseCallback<User>() {
            @Override
            public void onSuccess(User user) {
                SharedPreferences sp = getSharedPreferences("LocalData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("uid", user.getId());
                editor.putString("token", user.getToken());
                editor.apply();

                Intent i = new Intent(LoginActivity.this, GroupsActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
            }

        });
    }

    @OnClick(R.id.loginBtn)
    void onClickButton(View view) {
//        signIn(email.getText().toString(), password.getText().toString());
        signIn("haijima", "password1!");
    }

}

