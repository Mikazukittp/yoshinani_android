package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.ListView
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.GroupUserModel
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.InvitedGroupListAdapter
import butterknife.BindView
import butterknife.ButterKnife
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels
import rx.Observable
import java.util.*

/**
 * @author haijimakazuki
 */
class InvitedGroupDialogFragment : DialogFragment() {

    @BindView(R.id.list_view)
    internal var mListView: ListView? = null

    private val mInvitedGroups: List<GroupModel>? = null
    private var mBundle: Bundle? = null

    fun setBundle(bundle: Bundle) {
        mBundle = bundle
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 受け取った変数の初期化・整形
        var invitedGroups: List<GroupModel> = ArrayList()
        val parcelables = arguments!!.getParcelableArrayList<Parcelable>("invitedGroups")
        if (parcelables != null) {
            invitedGroups = Observable.from(parcelables)
                    .map<Any> { Parcels.unwrap(it) }
                    .cast<GroupModel>(GroupModel::class.java)
                    .toList()
                    .toBlocking().single()
        }

        val view = activity!!.layoutInflater.inflate(R.layout.dialog_invited_groups, null, false)
        ButterKnife.bind(this, view)
        mListView!!.adapter = InvitedGroupListAdapter(activity!!.applicationContext, invitedGroups)
        val builder = AlertDialog.Builder(activity!!)
                .setTitle("お知らせ")
                .setView(view)
                .setNegativeButton("閉じる") { dialog, id -> }

        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchDataEvent<GroupUserModel>) {
        EventBus.getDefault().post(RefreshEvent())
        dismiss()
    }

}