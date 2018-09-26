package app.android.ttp.mikazuki.yoshinani.view.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBar
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.gcm.RegistrationIntentService
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.view.fragment.AboutFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.AccountSettingFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.MainFragment
import butterknife.BindView
import butterknife.ButterKnife
import hotchemi.android.rate.AppRate

/**
 * @author haijimakazuki
 */
class MainActivity : BaseActivity() {
    @BindView(R.id.drawer_layout)
    internal var mDrawerLayout: DrawerLayout? = null
    @BindView(R.id.navigation)
    internal var mNavigationView: NavigationView? = null
    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null
    private var mDrawerToggle: ActionBarDrawerToggle? = null

    private val mGroups: List<GroupModel>? = null
    private var mUserService: UserService? = null
    private var me: UserModel? = null
    private val mInvitedGroups: List<GroupModel>? = null
    //    private Tracker mTracker;

    //    private BroadcastReceiver mRegistrationBroadcastReceiver;
    //    private boolean isReceiverRegistered;

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ButterKnife.bind(this)

        // ViewModelの宣言
        mUserService = UserService(applicationContext)

        // NavigationDrawerの設定
        setSupportActionBar(mToolbar)
        mDrawerToggle = ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name)
        mDrawerToggle!!.isDrawerIndicatorEnabled = true
        mDrawerLayout!!.setDrawerListener(mDrawerToggle)
        val actionBar = supportActionBar
        actionBar?.setDisplayShowHomeEnabled(true)
        mNavigationView!!.setNavigationItemSelectedListener(NavigationViewListener())

        val fragment = MainFragment()
        replaceFragment(fragment, R.id.fragment_container, false)
        refresh(true)

        // GCMの設定
        if (checkPlayServices()) {
            startService(Intent(this, RegistrationIntentService::class.java))
        }

        AppRate.with(this)
                .setInstallDays(10) // default 10, 0 means install day.
                .setLaunchTimes(10) // default 10 times.
                .setRemindInterval(2) // default 1 day.
                //                .setDebug(true) // default false.
                .setOnClickButtonListener { v -> Log.d(MainActivity::class.java!!.getName(), Integer.toString(v)) }
                .monitor()
        AppRate.showRateDialogIfMeetsConditions(this)
    }

    override fun onStart() {
        super.onStart()
        //        GoogleAnalytics.getInstance(this).reportActivityStart(this);
    }

    override fun onStop() {
        //        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        //        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        //        isReceiverRegistered = false;
        super.onPause()
    }

    override fun onDestroy() {
        //        mGroupViewModel.unsubscribe();
        super.onDestroy()
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        mDrawerToggle!!.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mDrawerToggle!!.onConfigurationChanged(newConfig)
    }

    override fun refresh(refreshForcibly: Boolean) {
        //        if (refreshForcibly || me == null) {
        mUserService!!.getMe()
        //        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show()
            } else {
                Log.i(TAG, "This device is not supported.")
                finish()
            }
            return false
        }
        return true
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchDataEvent<UserModel>) {
        if (!event.isType(UserModel::class.java!!)) {
            return
        }
        if (event.data == null) {
            return
        }
        me = event.data
        // NavigationDrawerのheaderの値を設定
        val navHeader = mNavigationView!!.getHeaderView(0)
        (navHeader.findViewById<View>(R.id.profile) as ImageView).setImageDrawable(me!!.icon)
        (navHeader.findViewById<View>(R.id.account) as TextView).text = me!!.account
        (navHeader.findViewById<View>(R.id.username) as TextView).text = me!!.username
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * 内部Listenerクラス
     */
    /* ------------------------------------------------------------------------------------------ */

    /**
     * Navigation Drawerでのメニュー選択時のListener
     */
    private inner class NavigationViewListener : NavigationView.OnNavigationItemSelectedListener {

        override fun onNavigationItemSelected(item: MenuItem): Boolean {
            //            item.setChecked(true);
            mDrawerLayout!!.closeDrawer(GravityCompat.START)

            var fragment: Fragment? = null
            val bundle = Bundle()
            when (item.itemId) {
                R.id.menu_top -> fragment = MainFragment()
                R.id.menu_account -> fragment = AccountSettingFragment()
                R.id.menu_about -> fragment = AboutFragment()
                R.id.menu_rate -> AppRate.with(this@MainActivity).showRateDialog(this@MainActivity)
                else -> return false
            }
            bundle.putParcelable("me", Parcels.wrap<UserModel>(me))
            if (fragment != null) {
                fragment.arguments = bundle
                replaceFragment(fragment, R.id.fragment_container)
            }
            return true
        }
    }

    companion object {

        private val TAG = MainActivity::class.java!!.getName()
    }

}
