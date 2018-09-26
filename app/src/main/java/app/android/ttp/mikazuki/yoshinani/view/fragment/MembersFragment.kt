package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.view.ViewCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.services.GroupService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.MemberListAdapter
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

class MembersFragment : Fragment() {

    @BindView(R.id.swipe_refresh)
    internal var mSwipeRefresh: SwipeRefreshLayout? = null
    @BindView(R.id.list_view)
    internal var mListView: ListView? = null
    private var mUnbinder: Unbinder? = null
    private var mUserModels: MutableList<UserModel>? = null
    private var mGroupService: GroupService? = null
    private var mGroupModel: GroupModel? = null

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_members, container, false)
        mUnbinder = ButterKnife.bind(this, view)

        mGroupModel = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))
        mGroupService = GroupService(activity!!.applicationContext)
        mGroupService!!.get(mGroupModel!!.id)

        mSwipeRefresh!!.setColorSchemeResources(R.color.theme600, R.color.accent600)
        mSwipeRefresh!!.setOnRefreshListener { mGroupService!!.get(mGroupModel!!.id) }
        ViewCompat.setNestedScrollingEnabled(mListView!!, true)

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

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchDataEvent<GroupModel>) {
        if (!event.isType(GroupModel::class.java!!)) {
            return
        }
        if (event.data != null) {
            val group = event.data
            mUserModels = group.members?.toMutableList()
            mUserModels!!.addAll(group.invitedMembers ?: arrayListOf())
            mListView!!.adapter = MemberListAdapter(activity!!.applicationContext, mUserModels
                    ?: mutableListOf(), mGroupModel!!)
        }
        if (mSwipeRefresh!!.isRefreshing) {
            mSwipeRefresh!!.isRefreshing = false
        }
    }

}
