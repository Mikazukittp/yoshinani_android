package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels
import java.util.Objects

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.event.ShowDialogEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.services.PaymentService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.view.DividerItemDecoration
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.PaymentListAdapter
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.PaymentDetailDialogFragment
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder

/**
 * A placeholder fragment containing a simple view.
 */
class PaymentLogFragment : Fragment() {

    @BindView(R.id.swipe_refresh)
    internal var mSwipeRefresh: SwipeRefreshLayout? = null
    @BindView(R.id.recycler_view)
    internal var mRecyclerView: RecyclerView? = null
    private var mUnbinder: Unbinder? = null

    private var mPaymentService: PaymentService? = null
    private var mGroupModel: GroupModel? = null
    private var mPayments: MutableList<PaymentModel>? = null
    private var mRecyclerViewAdapter: PaymentListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_payment_log, container, false)
        mUnbinder = ButterKnife.bind(this, view)

        mGroupModel = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))
        mPaymentService = PaymentService(activity!!.applicationContext)

        mSwipeRefresh!!.setColorSchemeResources(R.color.theme600, R.color.accent600)
        mSwipeRefresh!!.setOnRefreshListener { refresh() }
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity!!.applicationContext)
        mRecyclerView!!.addItemDecoration(DividerItemDecoration(activity!!.applicationContext))

        refresh()
        return view
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    fun refresh() {
        mSwipeRefresh!!.isRefreshing = true
        mPaymentService!!.getAll(mGroupModel!!.id)
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchListDataEvent<PaymentModel>) {
        if (event.tag == "first") {
            mPayments = event.listData
            mRecyclerViewAdapter = PaymentListAdapter(mRecyclerView!!, mPayments!!,
                    { v, position ->
                        val dialogEvent = ShowDialogEvent()
                        val bundle = Bundle()
                        bundle.putParcelable("payment", Parcels.wrap(mPayments!![position]))
                        dialogEvent.bundle = bundle
                        EventBus.getDefault().post(dialogEvent)
                        true
                    },
                    { mPaymentService!!.getNext(mGroupModel!!.id, mPayments!![mPayments!!.size - 2].id) }
            )
            mRecyclerView!!.adapter = mRecyclerViewAdapter
        } else if (event.tag == "next") {
            mRecyclerViewAdapter!!.addItems(event.listData)
            mRecyclerViewAdapter!!.removeItem(null)
        }
        //        }
        if (mSwipeRefresh!!.isRefreshing) {
            mSwipeRefresh!!.isRefreshing = false
        }
    }

    @Subscribe
    fun onEvent(event: ShowDialogEvent) {
        val manager = fragmentManager
        val dialog = PaymentDetailDialogFragment()
        dialog.arguments = event.bundle
        dialog.show(manager!!, "dialog")
    }

    @Subscribe
    fun onEvent(event: RefreshEvent) {
        refresh()
    }

    companion object {

        private val VISIBLE_THRESHOLD = 20
    }

}
