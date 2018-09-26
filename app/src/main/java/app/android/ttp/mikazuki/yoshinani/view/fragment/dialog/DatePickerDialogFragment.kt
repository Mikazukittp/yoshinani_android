package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.widget.DatePicker

import org.greenrobot.eventbus.EventBus

import java.util.Calendar
import java.util.GregorianCalendar

import app.android.ttp.mikazuki.yoshinani.event.DateSetEvent

/**
 * @author haijimakazuki
 */
class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    private val TAG = this.javaClass.getSimpleName()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val args = arguments
        val date = args!!.getSerializable("date") as Calendar
        return DatePickerDialog(activity!!, this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH))
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        EventBus.getDefault().post(DateSetEvent(GregorianCalendar(year, month, day)))
    }
}
