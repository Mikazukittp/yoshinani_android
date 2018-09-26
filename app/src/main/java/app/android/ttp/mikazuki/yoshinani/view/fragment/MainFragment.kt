package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.TotalModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils
import app.android.ttp.mikazuki.yoshinani.view.activity.GroupActivity
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.GroupListAdapter
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.AcceptDialogFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.GroupDetailDialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels
import rx.Observable
import java.util.*

/**
 * @author haijimakazuki
 */
class MainFragment : Fragment() {

    @BindView(R.id.swipe_refresh)
    internal var mSwipeRefresh: SwipeRefreshLayout? = null
    @BindView(R.id.list_view)
    internal var mListView: ListView? = null
    @BindView(R.id.total_amount)
    internal var mTotalAmount: TextView? = null
    @BindView(R.id.adView)
    internal var mAdView: AdView? = null
    private var mUnbinder: Unbinder? = null

    private var mGroups: MutableList<GroupModel>? = null
    private var mUserService: UserService? = null
    private var me: UserModel? = null
    private val mInvitedGroups: List<GroupModel>? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mUnbinder = ButterKnife.bind(this, view)

        // ListViewの設定
        mSwipeRefresh!!.setColorSchemeResources(R.color.theme600, R.color.accent600)
        mSwipeRefresh!!.setOnRefreshListener { EventBus.getDefault().post(RefreshEvent(true)) }
        ViewCompat.setNestedScrollingEnabled(mListView!!, true)
        val footer = activity!!.layoutInflater.inflate(R.layout.list_footer_group, null)
        footer.setOnClickListener { v -> GroupDetailDialogFragment().show(activity!!.supportFragmentManager, "createGroup") }
        mListView!!.addFooterView(footer)
        mListView!!.setOnItemClickListener { parent, v, position, id ->
            val bundle = Bundle()
            bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(mGroups!![position]))
            if (position < me!!.groups.size) {
                val event = ActivityTransitionEvent(GroupActivity::class.java)
                event.bundle = bundle
                EventBus.getDefault().post(event)
            } else {
                val dialog = AcceptDialogFragment()
                dialog.arguments = bundle
                dialog.show(activity!!.supportFragmentManager, "notification")
            }
        }

        // ViewModelの宣言
        mUserService = UserService(activity!!.applicationContext)

        // データの取得
        refresh()

        // Admobの配信
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device_id))
                .build()
        mAdView!!.loadAd(adRequest)
        return view
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.app_name)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    private fun refresh() {
        mSwipeRefresh!!.isRefreshing = true
        mUserService!!.getMe()
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchDataEvent<UserModel>) {
        if (!event.isType(UserModel::class.java!!)) {
            return
        }
        if (event.data == null) {
            return
        }

        me = event.data
        mGroups = ArrayList()
        mGroups!!.addAll(me!!.groups)
        mGroups!!.addAll(me!!.invitedGroups)
        // 毎回アクセスするためHashに持つ
        val totals = HashMap<Int, TotalModel>()
        for (total in me!!.totals) {
            totals[total.groupId] = total
        }
        mListView!!.adapter = GroupListAdapter(
                activity!!.application,
                GroupListAdapter.createGroupListItems(me!!.groups, me!!.invitedGroups, totals)
        )

        // 各グループでの支払額の合計を算出
        val totalAmount = Observable.from(mGroups!!)
                .map<Int> { it.id }
                .map<TotalModel> { totals[it] }
                .filter { total -> total != null }
                .map<Int> { it.result }
                .defaultIfEmpty(0)
                .reduce { sum, v -> sum!! + v!! }
                .toBlocking().single()
        mTotalAmount!!.text = TextUtils.wrapCurrency(totalAmount.toDouble())

        if (mSwipeRefresh!!.isRefreshing) {
            mSwipeRefresh!!.isRefreshing = false
        }
    }

    @Subscribe
    fun onEvent(event: RefreshEvent) {
        refresh()
    }
}
