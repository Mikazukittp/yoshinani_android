package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.crashlytics.android.Crashlytics;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.view.fragment.SignInFragment;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class LoginActivity extends BaseActivity {

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(getApplicationContext(), new Crashlytics());

        // ログイン済みならメイン画面へ遷移
        if (PreferenceUtil.isUserDataStored(this)) {
            goTo(MainActivity.class, false);
        }
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

//        FacebookSdk.sdkInitialize(getApplicationContext());

        SignInFragment fragment = new SignInFragment();
        replaceFragment(fragment, R.id.fragment_container, false);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
//        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
//        AppEventsLogger.deactivateApp(this);
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(ShowSnackbarEvent event) {
        Snackbar.make(findViewById(R.id.fragment_container), event.getmMessage(), Snackbar.LENGTH_SHORT).show();
    }
}

