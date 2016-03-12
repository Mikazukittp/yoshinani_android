package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.view.fragment.AboutFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.AccountSettingFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.MainFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class MainActivity extends BaseActivity {

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

            Fragment fragment;
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
                default:
                    return false;
            }
            bundle.putParcelable("me", Parcels.wrap(me));
            fragment.setArguments(bundle);
            replaceFragment(fragment, R.id.fragment_container);

            return true;
        }
    }

}
