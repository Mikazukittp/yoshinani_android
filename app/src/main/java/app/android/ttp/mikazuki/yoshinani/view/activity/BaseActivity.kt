package app.android.ttp.mikazuki.yoshinani.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IdRes
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity

import org.greenrobot.eventbus.Subscribe

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.ErrorEvent
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        refresh(false)
    }

    @JvmOverloads
    protected fun goTo(activity: Class<out Activity>,
                       addToBackStack: Boolean = true,
                       bundle: Bundle? = null) {
        val i = Intent(this, activity)
        if (addToBackStack) {
            i.flags = Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
        } else {
            i.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (bundle != null) {
            i.putExtras(bundle)
        }
        startActivity(i)
        if (!addToBackStack) {
            finish()
        }
    }

    @JvmOverloads
    protected fun replaceFragment(fragment: Fragment,
                                  @IdRes layoutId: Int,
                                  addToBackStack: Boolean = true,
                                  bundle: Bundle? = null) {
        if (bundle != null) {
            fragment.arguments = bundle
        }
        val transaction = supportFragmentManager
                .beginTransaction()
                .replace(layoutId, fragment, null)
        if (addToBackStack) {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    /**
     * Refresh data<br></br>
     * <br></br>
     * This method is called on [app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity.onResume] and [app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity.onEvent]<br></br>
     * refreshForcibly is true when called from [app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity.onEvent] and is false [app.android.ttp.mikazuki.yoshinani.view.activity.BaseActivity.onResume]<br></br>
     * <br></br>
     * This is blank method by default.
     * Concrete process should be implemented in Subclass
     *
     * @param refreshForcibly whether update forcibly or not
     */
    open fun refresh(refreshForcibly: Boolean) {}

    @Subscribe
    fun onEvent(event: ActivityTransitionEvent) {
        goTo(event.destinationActivity, event.doesAddToBackStack(), event.bundle)
    }

    @Subscribe
    fun onEvent(event: FragmentTransitionEvent) {
        replaceFragment(event.destinationFragment, R.id.fragment_container, event.doesAddToBackStack(), event.bundle)
    }

    @Subscribe
    fun onEvent(event: UnauthorizedEvent) {
        goTo(LoginActivity::class.java, false)
    }

    @Subscribe
    open fun onEvent(event: RefreshEvent) {
        refresh(true)
    }

    @Subscribe
    fun onEvent(event: ErrorEvent) {
        AlertDialog.Builder(this).setTitle(event.title)
                .setMessage(event.message)
                .setPositiveButton("閉じる", null)
                .create()
                .show()
    }


}
