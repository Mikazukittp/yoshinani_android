package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.EditText

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.databinding.DialogGroupDetailBinding
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.services.GroupService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import butterknife.BindView
import butterknife.ButterKnife

/**
 * @author haijimakazuki
 */
class GroupDetailDialogFragment : DialogFragment() {

    @BindView(R.id.name)
    internal var mName: EditText? = null
    @BindView(R.id.description)
    internal var mDescription: EditText? = null

    private var mGroupModel: GroupModel? = null
    private var mGroupService: GroupService? = null
    private var mBinding: DialogGroupDetailBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_group_detail, null, false)
        ButterKnife.bind(this, view)

        mGroupService = GroupService(activity!!.applicationContext)

        val title: String
        val positiveLabel: String
        if (arguments != null) {
            // 編集
            mGroupModel = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))
            title = "グループ編集"
            positiveLabel = "編集"

        } else {
            // 新規作成
            mGroupModel = GroupModel()
            title = "グループ新規作成"
            positiveLabel = "作成"
        }
        mBinding = DialogGroupDetailBinding.bind(view)
        mBinding!!.group = mGroupModel
        mGroupService = GroupService(activity!!.applicationContext)
        val builder = AlertDialog.Builder(activity!!)
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveLabel) { dialog, which ->
                    if (arguments != null) {
                        mGroupService!!.update(mGroupModel!!)
                    } else {
                        mGroupService!!.create(mGroupModel!!)
                    }
                }
                .setNegativeButton("キャンセル") { dialog, which -> dismiss() }
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
    fun onEvent(event: FetchDataEvent<GroupModel>) {
        EventBus.getDefault().post(RefreshEvent())
        dismiss()
    }

}