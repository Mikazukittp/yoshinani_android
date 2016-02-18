package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.view.adapter.pager.GroupPagerAdapter;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.GroupDetailDialogFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserSearchDialogFragment;
import app.android.ttp.mikazuki.yoshinani.viewModel.GroupViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * @author haijimakazuki
 */
public class GroupActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.fab)
    FloatingActionButton mFab;

    private GroupViewModel mGroupViewModel;
    private GroupModel mGroupModel;

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);
        ButterKnife.bind(this);

        mGroupViewModel = new GroupViewModel(getApplicationContext());
        mGroupModel = Parcels.unwrap(getIntent().getExtras().getParcelable(Constants.BUNDLE_GROUP_KEY));
        mGroupViewModel.get(mGroupModel.getId());

        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        setTitle(mGroupModel.getName().get());
        mToolbar.setSubtitle(mGroupModel.getDescription().get());

        mViewPager.addOnPageChangeListener(new GroupViewPagerListener());
        initTabLayout();
        mFab.hide();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        FragmentManager manager = getSupportFragmentManager();
        DialogFragment dialog;
        final Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(mGroupModel));
        switch (item.getItemId()) {
            case R.id.menu_add_member:
                dialog = new UserSearchDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "search");
                return true;
            case R.id.menu_edit_group:
                dialog = new GroupDetailDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(manager, "createGroup");
                return true;
        }
        return false;
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    private void initTabLayout() {
        tabLayout.setBackgroundColor(getResources().getColor(R.color.theme600));
        PagerAdapter pagerAdapter = new GroupPagerAdapter(GroupActivity.this, mViewPager, getIntent().getExtras());
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    public void onEvent(FetchDataEvent<GroupModel> event) {
        mGroupModel = event.getData();
        setTitle(mGroupModel.getName().get());
        mToolbar.setSubtitle(mGroupModel.getDescription().get());
    }

    public void onEvent(RefreshEvent event) {
        mGroupViewModel.get(mGroupModel.getId());
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * OnClick methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.fab)
    public void onButtonPressed(View v) {
        ActivityTransitionEvent event = new ActivityTransitionEvent(PostActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(mGroupModel));
        event.setBundle(bundle);
        EventBus.getDefault().post(event);
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * 内部Listenerクラス
     */
    /* ------------------------------------------------------------------------------------------ */

    /**
     * タブ切り替えの際のFABの挙動を規定するListener
     */
    private class GroupViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case 1:
                    mFab.show();
                    break;
                default:
                    mFab.hide();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

