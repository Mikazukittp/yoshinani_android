package app.android.ttp.mikazuki.yoshinani.view.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.MenuItem

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.view.adapter.pager.PostPagerAdapter
import butterknife.BindView
import butterknife.ButterKnife

class PostActivity : BaseActivity() {

    @BindView(R.id.tool_bar)
    internal var mToolbar: Toolbar? = null
    @BindView(R.id.tab_layout)
    internal var tabLayout: TabLayout? = null
    @BindView(R.id.view_pager)
    internal var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        ButterKnife.bind(this)

        mToolbar!!.setBackgroundColor(resources.getColor(R.color.grey400))
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        initTabLayout()
    }

    private fun initTabLayout() {
        tabLayout!!.setBackgroundColor(resources.getColor(R.color.grey400))
        val pagerAdapter = PostPagerAdapter(this@PostActivity, mViewPager!!, intent.extras)
        tabLayout!!.setTabsFromPagerAdapter(pagerAdapter)
        tabLayout!!.setupWithViewPager(mViewPager)
        tabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        tabLayout!!.tabMode = TabLayout.MODE_FIXED
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            overridePendingTransition(R.anim.stay, R.anim.out_bottom)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.stay, R.anim.out_bottom)
    }

}
