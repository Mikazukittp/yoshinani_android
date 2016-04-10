package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.gcm.RegistrationIntentService;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.view.fragment.AboutFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.AccountSettingFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.MainFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import hotchemi.android.rate.AppRate;

/**
 * @author haijimakazuki
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getName();
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation)
    NavigationView mNavigationView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<GroupModel> mGroups;
    private UserService mUserService;
    private UserModel me;
    private List<GroupModel> mInvitedGroups;
//    private Tracker mTracker;

//    private BroadcastReceiver mRegistrationBroadcastReceiver;
//    private boolean isReceiverRegistered;

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // ViewModelの宣言
        mUserService = new UserService(getApplicationContext());

        // NavigationDrawerの設定
        setSupportActionBar(mToolbar);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.app_name, R.string.app_name);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mNavigationView.setNavigationItemSelectedListener(new NavigationViewListener());

        MainFragment fragment = new MainFragment();
        replaceFragment(fragment, R.id.fragment_container, false);
        refresh(true);

        // GCMの設定
        if (checkPlayServices()) {
            startService(new Intent(this, RegistrationIntentService.class));
        }

        AppRate.with(this)
                .setInstallDays(10) // default 10, 0 means install day.
                .setLaunchTimes(10) // default 10 times.
                .setRemindInterval(2) // default 1 day.
//                .setDebug(true) // default false.
                .setOnClickButtonListener(v -> Log.d(MainActivity.class.getName(), Integer.toString(v)))
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);
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
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        isReceiverRegistered = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//        mGroupViewModel.unsubscribe();
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void refresh(final boolean refreshForcibly) {
//        if (refreshForcibly || me == null) {
        mUserService.getMe();
//        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, 9000)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(FetchDataEvent<UserModel> event) {
        if (!event.isType(UserModel.class)) {
            return;
        }
        if (event.getData() == null) {
            return;
        }
        me = event.getData();
        // NavigationDrawerのheaderの値を設定
        View navHeader = mNavigationView.getHeaderView(0);
        ((ImageView) navHeader.findViewById(R.id.profile)).setImageDrawable(me.getIcon());
        ((TextView) navHeader.findViewById(R.id.account)).setText(me.getAccount());
        ((TextView) navHeader.findViewById(R.id.username)).setText(me.getUsername());
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * 内部Listenerクラス
     */
    /* ------------------------------------------------------------------------------------------ */

    /**
     * Navigation Drawerでのメニュー選択時のListener
     */
    private class NavigationViewListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
//            item.setChecked(true);
            mDrawerLayout.closeDrawer(GravityCompat.START);

            Fragment fragment = null;
            Bundle bundle = new Bundle();
            switch (item.getItemId()) {
                case R.id.menu_top:
                    fragment = new MainFragment();
                    break;
                case R.id.menu_account:
                    fragment = new AccountSettingFragment();
                    break;
                case R.id.menu_about:
                    fragment = new AboutFragment();
                    break;
                case R.id.menu_rate:
                    AppRate.with(MainActivity.this).showRateDialog(MainActivity.this);
                    break;
                default:
                    return false;
            }
            bundle.putParcelable("me", Parcels.wrap(me));
            if (fragment != null) {
                fragment.setArguments(bundle);
                replaceFragment(fragment, R.id.fragment_container);
            }
            return true;
        }
    }

}
