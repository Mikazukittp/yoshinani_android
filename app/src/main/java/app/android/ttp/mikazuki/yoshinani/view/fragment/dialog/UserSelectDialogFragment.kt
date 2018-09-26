package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import org.greenrobot.eventbus.EventBus

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.UserSelectEvent

class UserSelectDialogFragment : DialogFragment() {

    private var mSelected: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 受け取った変数の初期化・整形
        var items = arguments!!.getCharSequenceArray("users")
        if (items == null) {
            items = arrayOfNulls(0)
        }
        mSelected = arguments!!.getInt("selected", -1)

        val builder = AlertDialog.Builder(activity!!)
                .setTitle("返済相手")
                .setSingleChoiceItems(items, mSelected) { dialog: DialogInterface, which: Int -> mSelected = which }
                .setPositiveButton(R.string.ok) { dialog: DialogInterface, id: Int -> EventBus.getDefault().post(UserSelectEvent(mSelected)) }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, id: Int -> }

        return builder.create()
    }

}