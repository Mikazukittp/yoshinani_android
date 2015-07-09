package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.PostFragment;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.PostPaymentFragment;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.PostRepaymentFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

public class PostActivity extends AppCompatActivity implements PostPaymentFragment.OnFragmentInteractionListener, PostRepaymentFragment.OnFragmentInteractionListener{

    @Bind(R.id.view_pager)
    ViewPager mViewPager;
    @Bind(R.id.tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.tab_layout)
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        initTabLayout();
    }


    private void initTabLayout() {
        tabLayout.setBackgroundColor(getResources().getColor(R.color.theme600));
        PagerAdapter pagerAdapter = new PagerAdapter(PostActivity.this, mViewPager);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    static class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        private static final int PAGE_COUNT = 2;

        private Context mContext;
        private ViewPager mViewPager;

        public PagerAdapter(AppCompatActivity activity, ViewPager viewPager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mViewPager = viewPager;
            mViewPager.setAdapter(this);
            mViewPager.addOnPageChangeListener(this);
        }


        @Override
        public Fragment getItem(int position) {
            PostFragment fragment;
            switch (position) {
                case 0:
                    fragment = PostPaymentFragment.newInstance();
                    break;
                case 1:
                    fragment = PostRepaymentFragment.newInstance();
                    break;
                default:
                    fragment = PostPaymentFragment.newInstance();
            }
            return fragment;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return mContext.getString(R.string.payment);
                case 1:
                    return mContext.getString(R.string.repayment);
                default:
                    return mContext.getString(R.string.payment);
            }
        }
    }
}
