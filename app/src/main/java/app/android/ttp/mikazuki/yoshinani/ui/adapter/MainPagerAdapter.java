package app.android.ttp.mikazuki.yoshinani.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.LogFragment;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.MainFragment;
import app.android.ttp.mikazuki.yoshinani.ui.fragment.OverviewFragment;

/**
 * Created by haijimakazuki on 15/07/10.
 */
public class MainPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private static final int PAGE_COUNT = 2;

    private Context mContext;
    private ViewPager mViewPager;

    public MainPagerAdapter(AppCompatActivity activity, ViewPager viewPager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
    }


    @Override
    public Fragment getItem(int position) {
        MainFragment fragment;
        switch (position) {
            case 0:
                fragment = LogFragment.newInstance();
                break;
            case 1:
                fragment = OverviewFragment.newInstance();
                break;
            default:
                fragment = LogFragment.newInstance();
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
                return mContext.getString(R.string.log);
            case 1:
                return mContext.getString(R.string.overview);
            default:
                return mContext.getString(R.string.log);
        }
    }
}

