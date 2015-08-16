package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.ui.adapter.PostPagerAdapter;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.PostRepaymentFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity implements PostRepaymentFragment.OnFragmentInteractionListener{

    @Bind(R.id.tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        mToolbar.setBackgroundColor(getResources().getColor(R.color.gray400));
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initTabLayout();
    }


    private void initTabLayout() {
        tabLayout.setBackgroundColor(getResources().getColor(R.color.gray400));
        PagerAdapter pagerAdapter = new PostPagerAdapter(PostActivity.this, mViewPager);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            overridePendingTransition(R.anim.stay, R.anim.out_bottom);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.out_bottom);
    }

}
