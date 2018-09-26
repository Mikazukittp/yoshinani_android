package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.GroupUserModel
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.services.GroupService

/**
 * @author haijimakazuki
 */
class AcceptDialogFragment : DialogFragment() {

    private var mGroupService: GroupService? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 受け取った変数の初期化・整形
        val invitedGroup = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))
        mGroupService = GroupService(activity!!.applicationContext)

        val builder = AlertDialog.Builder(activity!!)
                .setTitle(String.format("%s への招待", invitedGroup.name.get()))
                .setMessage(invitedGroup.description.get())
                .setPositiveButton("承認", { dialog, id -> mGroupService!!.accept(invitedGroup.id) })
                .setNeutralButton("閉じる", { dialog, id -> })
                .setNegativeButton("拒否", { dialog, id -> })

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