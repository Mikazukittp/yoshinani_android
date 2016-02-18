package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.android.ttp.mikazuki.yoshinani.BaseApplication;
import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.TotalModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils;
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.GroupListAdapter;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.GroupDetailDialogFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.InvitedGroupDialogFragment;
import app.android.ttp.mikazuki.yoshinani.viewModel.UserViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.Observable;

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
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.list_view)
    ListView mListView;
    @Bind(R.id.total_amount)
    TextView mTotalAmount;
    private ActionBarDrawerToggle mDrawerToggle;

    private List<GroupModel> mGroups;
    private UserViewModel mUserViewModel;
    private UserModel me;
    private List<GroupModel> mInvitedGroups;
    private Tracker mTracker;

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

        // ListViewの設定
        mSwipeRefresh.setColorSchemeResources(R.color.theme600, R.color.accent500);
        mSwipeRefresh.setOnRefreshListener(() -> refresh(true));
        ViewCompat.setNestedScrollingEnabled(mListView, true);
        mListView.setOnItemClickListener((parent, view, position, id) -> {
            ActivityTransitionEvent event = new ActivityTransitionEvent(GroupActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(mGroups.get(position)));
            event.setBundle(bundle);
            EventBus.getDefault().post(event);
        });

        // ViewModelの宣言
        mUserViewModel = new UserViewModel(this);
//        mGroupViewModel = new GroupViewModel(this);

        // データの取得
        mUserViewModel.getMe();
//        mGroupViewModel.getAll(Integer.parseInt(PreferenceUtil.getUid(getApplicationContext())));

        // Admobの配信
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("417B222F70913CAC12ECE9B9DD3EABE8").build();
        mAdView.loadAd(adRequest);

        // Analyticsの設定
        // Obtain the shared Tracker instance.
        BaseApplication application = (BaseApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("メイン画面だよ");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
//        mTracker.send(new HitBuilders.EventBuilder()
//                .setCategory("Category")
//                .setAction("Action")
//                .setLabel("Label")
//                .build());
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar, menu);
//        // お知らせ未読件数バッジ表示
//        MenuItem item = menu.findItem(R.id.menu_notification);
//        MenuItemCompat.setActionView(item, R.layout.notification_count);
//        View view = MenuItemCompat.getActionView(item);
//        final TextView countLabel = (TextView) view.findViewById(R.id.notification_count);
//
//        int unreadNotificationCount = mInvitedGroups != null ? mInvitedGroups.size() : 0;
//        if (unreadNotificationCount > 0) {
//            countLabel.setText(String.valueOf(unreadNotificationCount));
//        } else {
//            countLabel.setVisibility(View.INVISIBLE);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_notification:
                if (mInvitedGroups != null && !mInvitedGroups.isEmpty()) {
                    FragmentManager manager = getSupportFragmentManager();
                    InvitedGroupDialogFragment dialog = new InvitedGroupDialogFragment();
                    final Bundle bundle = new Bundle();
                    ArrayList<Parcelable> wrapped = Lists.newArrayList(Observable.from(mInvitedGroups).map(Parcels::wrap).toList().toBlocking().single());
                    bundle.putParcelableArrayList("invitedGroups", wrapped);
                    dialog.setArguments(bundle);
                    dialog.show(manager, "notification");
                }
                return true;
        }
        return false;
    }

    @Override
    public void refresh(final boolean refreshForcibly) {
        if (refreshForcibly || me == null) {
            mUserViewModel.getMe();
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    public void onEvent(FetchDataEvent<UserModel> event) {
        if (!event.isType(UserModel.class)) {
            return;
        }

        me = event.getData();
        mGroups = me.getGroups();
        Map<Integer, TotalModel> totals = Maps.newHashMap();
        // 毎回アクセスするためHashに持つ
        for (TotalModel total : me.getTotals()) {
            totals.put(total.getGroupId(), total);
        }

        // 各グループでの支払額の合計を算出
        final int totalAmount = Observable.from(mGroups)
                .map(GroupModel::getId)
                .map(totals::get)
                .filter(total -> total != null)
                .map(TotalModel::getResult)
                .defaultIfEmpty(0)
                .reduce((sum, v) -> sum + v)
                .toBlocking().single();
        mTotalAmount.setText(TextUtils.wrapCurrency(totalAmount));
        mListView.setAdapter(new GroupListAdapter(getApplicationContext(), mGroups, totals));

        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }

        // グループへの招待をmenuに描画
        mInvitedGroups = me.getInvitedGroups();
        supportInvalidateOptionsMenu();
    }

    public void onEvent(RefreshEvent event) {
        mUserViewModel.getMe();
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
            item.setChecked(true);
            switch (item.getItemId()) {
                case R.id.menu_add_group:
                    FragmentManager manager = getSupportFragmentManager();
                    GroupDetailDialogFragment dialog = new GroupDetailDialogFragment();
                    dialog.show(manager, "createGroup");
                    break;
                case R.id.menu_edit_profile:
                    break;
                case R.id.menu_sign_out:
                    PreferenceUtil.clearUserData(getApplicationContext());
                    EventBus.getDefault().post(new UnauthorizedEvent());
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

}
