package app.android.ttp.mikazuki.yoshinani.view.adapter.pager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.view.fragment.PostFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.PostPaymentFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.PostRepaymentFragment

/**
 * Created by haijimakazuki on 15/07/10.
 */
class PostPagerAdapter @JvmOverloads constructor(activity: AppCompatActivity,
                                                 private val mViewPager: ViewPager,
                                                 private val mBundle: Bundle? = null) : FragmentPagerAdapter(activity.supportFragmentManager), ViewPager.OnPageChangeListener {

    private val mContext: Context

    init {
        mContext = activity
        mViewPager.adapter = this
        mViewPager.addOnPageChangeListener(this)
    }

    override fun getItem(position: Int): Fragment {
        val fragment: PostFragment
        when (position) {
            0 -> fragment = PostPaymentFragment()
            1 -> fragment = PostRepaymentFragment()
            else -> fragment = PostPaymentFragment()
        }
        fragment.arguments = mBundle
        return fragment
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun getCount(): Int {
        return PAGE_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        when (position) {
            0 -> return mContext.getString(R.string.payment)
            1 -> return mContext.getString(R.string.repayment)
            else -> return mContext.getString(R.string.payment)
        }
    }

    companion object {

        private val PAGE_COUNT = 2
    }
}