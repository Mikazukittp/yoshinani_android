package app.android.ttp.mikazuki.yoshinani.view.activity

import android.os.Bundle
import android.support.design.widget.Snackbar

import com.crashlytics.android.Crashlytics

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.view.fragment.SignInFragment
import butterknife.ButterKnife
import io.fabric.sdk.android.Fabric

class LoginActivity : BaseActivity() {

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(applicationContext, Crashlytics())

        // ログイン済みならメイン画面へ遷移
        if (PreferenceUtil.isUserDataStored(this)) {
            goTo(MainActivity::class.java, false)
        }
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)

        //        FacebookSdk.sdkInitialize(getApplicationContext());

        val fragment = SignInFragment()
        replaceFragment(fragment, R.id.fragment_container, false)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
        //        AppEventsLogger.activateApp(this);
    }

    override fun onPause() {
        //        AppEventsLogger.deactivateApp(this);
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: ShowSnackbarEvent) {
        Snackbar.make(findViewById<View>(R.id.fragment_container), event.message, Snackbar.LENGTH_SHORT).show()
    }
}

