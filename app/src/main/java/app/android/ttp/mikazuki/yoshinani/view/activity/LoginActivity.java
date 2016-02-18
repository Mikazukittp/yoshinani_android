package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;

import com.crashlytics.android.Crashlytics;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.view.fragment.SignInFragment;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
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

        SignInFragment fragment = new SignInFragment();
        replaceFragment(fragment, R.id.fragment_container, false);

//        ((BaseApplication) getApplication()).getDefaultTracker();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    @Override
    protected void onStop() {
//        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
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
    public void onEvent(ShowSnackbarEvent event) {
        Snackbar.make(findViewById(R.id.fragment_container), event.getmMessage(), Snackbar.LENGTH_SHORT).show();
    }
}

