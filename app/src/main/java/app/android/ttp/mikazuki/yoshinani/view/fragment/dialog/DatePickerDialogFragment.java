package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.GregorianCalendar;

import app.android.ttp.mikazuki.yoshinani.event.DateSetEvent;
import de.greenrobot.event.EventBus;

/**
 * @author haijimakazuki
 */
public class DatePickerDialogFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        Calendar date = (Calendar) args.getSerializable("date");
        return new DatePickerDialog(getActivity(), this, date.get(Calendar.YEAR), date.get(Calendar.MONTH), date.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        EventBus.getDefault().post(new DateSetEvent(new GregorianCalendar(year, month, day)));
    }
}
