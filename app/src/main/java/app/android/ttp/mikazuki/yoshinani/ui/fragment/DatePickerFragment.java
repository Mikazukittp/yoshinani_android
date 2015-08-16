package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by haijimakazuki on 15/07/17.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final String TAG = "DatePickerFragment";

    DatePickerDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        try {
            mListener = (DatePickerDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            Log.e(TAG, "Callback of this class must be implemented by target fragment: " + getTargetFragment().getClass().getSimpleName(), e);
            throw e;
        }
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        if (mListener != null) {
            mListener.setDate(year, month, day);
        } else {
            Log.e(TAG, "Callback was null.");
        }
    }

    public interface DatePickerDialogListener {
        public void setDate(int year, int month, int day);
    }

}
