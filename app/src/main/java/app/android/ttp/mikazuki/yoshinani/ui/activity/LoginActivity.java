package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitAuthRepository;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitUserRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Token;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.AuthRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.email)
    EditText email;
    @Bind(R.id.password)
    EditText password;

    private AuthRepository mAuthRepository;
    private UserRepository mUserRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuthRepository = new RetrofitAuthRepository();
        mUserRepository = new RetrofitUserRepository();
    }


    private void signIn(String email, String password) {

        mAuthRepository.signIn(email, password, new BaseCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                SharedPreferences sp = getSharedPreferences("LocalData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", token.getToken());
                editor.apply();

                mUserRepository.getMe(new BaseCallback<User>() {
                    @Override
                    public void onSuccess(User user) {
                        SharedPreferences sp = getSharedPreferences("LocalData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("myId", user.get_id());
                        editor.apply();

                        Intent i = new Intent(LoginActivity.this, MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
                    }

                    @Override
                    public void onFailure() {
                    }
                });
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
        signIn("haijima@r.recruit.co.jp", "haijima");
    }

}

