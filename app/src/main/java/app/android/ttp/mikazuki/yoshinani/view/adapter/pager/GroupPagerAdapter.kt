package app.android.ttp.mikazuki.yoshinani.view.adapter.pager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import org.parceler.Parcels

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.view.fragment.MembersFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.PaymentLogFragment

/**
 * @author haijimakazuki
 */
class GroupPagerAdapter(activity: AppCompatActivity,
                        private val mViewPager: ViewPager) : FragmentPagerAdapter(activity.supportFragmentManager), ViewPager.OnPageChangeListener {

    private val mContext: Context
    private var mGroup: GroupModel? = null

    init {
        mContext = activity
        mViewPager.adapter = this
        mViewPager.addOnPageChangeListener(this)
    }

    override fun getItem(position: Int): Fragment {
        val fragment: Fragment
        when (position) {
            0 -> fragment = MembersFragment()
            1 -> fragment = PaymentLogFragment()
            else -> fragment = MembersFragment()
        }
        val bundle = Bundle()
        bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap<GroupModel>(mGroup))
        fragment.arguments = bundle
        return fragment
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val members = if (mGroup!!.members != null) mGroup!!.members.size else 0
        when (position) {
            0 -> return mContext.getString(R.string.membersWithNum, members)
            1 -> return mContext.getString(R.string.log)
            else -> return mContext.getString(R.string.membersWithNum, members)
        }
    }

    fun setGroup(group: GroupModel) {
        mGroup = group
    }

    companion object {

        private val PAGE_COUNT = 2
    }

}

