package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitAuthRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Token;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.AuthRepository;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.MainFragment;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.SubFragment;
import app.android.ttp.mikazuki.yoshinani.ui.listener.ToolBarListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements ToolBarListener, MainFragment.OnFragmentInteractionListener, SubFragment.OnFragmentInteractionListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation)
    NavigationView mNavigationView;
    EditText email;
    EditText password;
    private AuthRepository mAuthRepository;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean network;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuthRepository = new RetrofitAuthRepository();

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                int itemId = menuItem.getItemId();
                if (itemId == R.id.menu_main) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new MainFragment())
                            .addToBackStack(null)
                            .commit();
                    mDrawerLayout.closeDrawers();
                    return true;
                } else if (itemId == R.id.menu_sub) {
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, new SubFragment())
                            .addToBackStack(null)
                            .commit();
                    mDrawerLayout.closeDrawers();
                    return true;
                }
                return false;
            }
        });
    }


    private void setListData(String email, String password) {

        mAuthRepository.signIn(email, password, new BaseCallback<Token>() {
            @Override
            public void onSuccess(Token token) {
                Log.e("!!!!!", "success!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                SharedPreferences sp = getSharedPreferences("LocalData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("token", token.getToken());
                editor.apply();

                SharedPreferences sp2 = getSharedPreferences("LocalData", Context.MODE_PRIVATE);
                String token2 = sp2.getString("token", "");
                String accessToken = "Bearer " + token2;
                System.out.println(accessToken);

                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
            }

        });
    }

    @OnClick(R.id.loginBtn)
    void onClickButton() {
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        setListData(email.getText().toString(), password.getText().toString());
    }

    @Override
    public void goToMainView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MainFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToSubView() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new SubFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMenuClicked() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

}

