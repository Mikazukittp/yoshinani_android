package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog

import org.greenrobot.eventbus.EventBus

import java.util.ArrayList

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.UserMultiSelectEvent

class UserMultiSelectDialogFragment : DialogFragment() {

    private var mSelected: ArrayList<Int>? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 受け取った変数の初期化・整形
        var items = arguments!!.getCharSequenceArray("users")
        if (items == null) {
            items = arrayOfNulls(0)
        }
        mSelected = arguments!!.getIntegerArrayList("selected")
        if (mSelected == null) {
            mSelected = ArrayList()
        }

        val selectedFlags = BooleanArray(items.size)
        for (i in mSelected!!) {
            selectedFlags[i] = true
        }

        val builder = AlertDialog.Builder(activity!!)
                .setTitle(R.string.dialog_participants_title)
                .setMultiChoiceItems(items, selectedFlags) { dialog: DialogInterface, which: Int, isChecked: Boolean ->
                    if (isChecked) {
                        mSelected!!.add(which)
                    } else {
                        mSelected!!.remove(Integer.valueOf(which))
                    }
                }
                .setPositiveButton(R.string.ok) { dialog: DialogInterface, id: Int -> EventBus.getDefault().post(UserMultiSelectEvent(mSelected)) }
                .setNegativeButton(R.string.cancel) { dialog: DialogInterface, id: Int -> }

        return builder.create()
    }

}