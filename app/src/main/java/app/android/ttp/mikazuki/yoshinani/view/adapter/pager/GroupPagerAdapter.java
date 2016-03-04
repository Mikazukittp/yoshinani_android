package app.android.ttp.mikazuki.yoshinani.view.adapter.pager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
 * @author haijimakazuki
 */
public class GroupPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private static final int PAGE_COUNT = 2;

    private Context mContext;
    private ViewPager mViewPager;
    private GroupModel mGroup;

    public GroupPagerAdapter(@NonNull final AppCompatActivity activity,
                             @NonNull final ViewPager viewPager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mViewPager = viewPager;
        mViewPager.setAdapter(this);
        mViewPager.addOnPageChangeListener(this);
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
        final Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(mGroup));
        fragment.setArguments(bundle);
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
    public CharSequence getPageTitle(final int position) {
        final int members = mGroup.getMembers() != null ? mGroup.getMembers().size() : 0;
        switch (position) {
            case 0:
                return mContext.getString(R.string.membersWithNum, members);
            case 1:
                return mContext.getString(R.string.log);
            default:
                return mContext.getString(R.string.membersWithNum, members);
        }
    }

    public void setGroup(GroupModel group) {
        mGroup = group;
    }

}

