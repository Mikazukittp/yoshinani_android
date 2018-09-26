package app.android.ttp.mikazuki.yoshinani.view.activity

import android.os.Bundle
import android.os.Parcelable
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.DialogFragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.services.GroupService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.view.adapter.pager.GroupPagerAdapter
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.GroupDetailDialogFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserSearchDialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

/**
 * @author haijimakazuki
 */
class GroupActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null
    @BindView(R.id.tab_layout)
    internal var mTabLayout: TabLayout? = null
    @BindView(R.id.view_pager)
    internal var mViewPager: ViewPager? = null
    @BindView(R.id.fab)
    internal var mFab: FloatingActionButton? = null

    private var mGroupService: GroupService? = null
    private lateinit var mGroupModel: GroupModel
    private var mPagerAdapter: GroupPagerAdapter? = null

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group)
        ButterKnife.bind(this)

        mGroupService = GroupService(applicationContext)
        mGroupModel = Parcels.unwrap<GroupModel>(intent.extras!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))
        mGroupService!!.get(mGroupModel!!.id)

        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        mToolbar!!.setNavigationOnClickListener { v -> onBackPressed() }
        title = mGroupModel!!.name.get()
        mToolbar!!.subtitle = mGroupModel!!.description.get()

        mViewPager!!.addOnPageChangeListener(GroupViewPagerListener())
        initTabLayout()
        mFab!!.hide()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.group_toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val manager = supportFragmentManager
        val dialog: DialogFragment
        val bundle = Bundle()
        bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap<GroupModel>(mGroupModel))
        when (item.itemId) {
            R.id.menu_add_member -> {
                dialog = UserSearchDialogFragment()
                dialog.setArguments(bundle)
                dialog.show(manager, "search")
                return true
            }
            R.id.menu_edit_group -> {
                dialog = GroupDetailDialogFragment()
                dialog.setArguments(bundle)
                dialog.show(manager, "createGroup")
                return true
            }
        }
        return false
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    private fun initTabLayout() {
        mTabLayout!!.setBackgroundColor(resources.getColor(R.color.theme600))
        mPagerAdapter = GroupPagerAdapter(this@GroupActivity, mViewPager!!)
        mPagerAdapter!!.setGroup(mGroupModel)
        mTabLayout!!.setTabsFromPagerAdapter(mPagerAdapter)
        mTabLayout!!.setupWithViewPager(mViewPager)
        mTabLayout!!.tabGravity = TabLayout.GRAVITY_FILL
        mTabLayout!!.tabMode = TabLayout.MODE_FIXED
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchDataEvent<GroupModel>) {
        if (!event.isType(GroupModel::class.java)) {
            return
        }
        mGroupModel = event.data ?: throw NullPointerException("GroupModel got from Event is null.")
        title = mGroupModel.name.get()
        mToolbar!!.subtitle = mGroupModel.description.get()
        mPagerAdapter!!.setGroup(mGroupModel)
        mTabLayout!!.setTabsFromPagerAdapter(mPagerAdapter)
    }

    @Subscribe
    override fun onEvent(event: RefreshEvent) {
        mGroupService!!.get(mGroupModel.id)
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * OnClick methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.fab)
    fun onButtonPressed(v: View) {
        val event = ActivityTransitionEvent(PostActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap<GroupModel>(mGroupModel))
        event.bundle = bundle
        EventBus.getDefault().post(event)
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * 内部Listenerクラス
     */
    /* ------------------------------------------------------------------------------------------ */

    /**
     * タブ切り替えの際のFABの挙動を規定するListener
     */
    private inner class GroupViewPagerListener : ViewPager.OnPageChangeListener {

        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

        override fun onPageSelected(position: Int) {
            when (position) {
                1 -> mFab!!.show()
                else -> mFab!!.hide()
            }
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
}

