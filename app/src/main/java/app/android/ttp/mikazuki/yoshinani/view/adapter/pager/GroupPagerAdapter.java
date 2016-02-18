package app.android.ttp.mikazuki.yoshinani.view.adapter.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.view.fragment.MembersFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.PaymentLogFragment;

/**
 * Created by haijimakazuki on 15/07/10.
 */
public class GroupPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private static final int PAGE_COUNT = 2;
    private final Bundle mBundle;

    private Context mContext;
    private ViewPager mViewPager;

    public GroupPagerAdapter(@NonNull final AppCompatActivity activity,
                             @NonNull final ViewPager viewPager) {
        this(activity, viewPager, null);
    }

    public GroupPagerAdapter(@NonNull final AppCompatActivity activity,
                             @NonNull final ViewPager viewPager,
                             @Nullable final Bundle bundle) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
        mBundle = bundle;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = new MembersFragment();
                break;
            case 1:
                fragment = new PaymentLogFragment();
                break;
            default:
                fragment = new MembersFragment();
        }
        fragment.setArguments(mBundle);
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
        GroupModel group = Parcels.unwrap(mBundle.getParcelable(Constants.BUNDLE_GROUP_KEY));
        switch (position) {
            case 0:
                return mContext.getString(R.string.membersWithNum, 0);
            case 1:
                return mContext.getString(R.string.log);
            default:
                return mContext.getString(R.string.membersWithNum, group.getMembers().size());
        }
    }
}

