package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.util.ArrayList;

import app.android.ttp.mikazuki.yoshinani.R;

public class UserDialogFragment extends DialogFragment {

    private static final String TAG = "UserDialogFragment";
    private ArrayList<Integer> mSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 受け取った変数の初期化・整形
        CharSequence[] items = getArguments().getCharSequenceArray("users");
        if (items == null) {
            items = new CharSequence[0];
        }
        if (getArguments().getIntegerArrayList("selected") == null) {
            mSelected = new ArrayList<Integer>();
        } else {
            mSelected = (ArrayList<Integer>) getArguments().getIntegerArrayList("selected").clone();
        }

        boolean[] selectedFlags = new boolean[items.length];
        for (Integer i : mSelected) {
            selectedFlags[i] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_participants_title)
                .setMultiChoiceItems(items, selectedFlags, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            mSelected.add(which);
                        } else if (mSelected.contains(which)) {
                            mSelected.remove(Integer.valueOf(which));
                        }
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        PostPaymentFragment target;
                        try {
                            target = (PostPaymentFragment) getTargetFragment();
                        } catch (ClassCastException e) {
                            Log.e(TAG, e.getMessage());
                            dismiss();
                            return;
                        }
                        if (target == null) {
                            dismiss();
                            return;
                        }
                        target.setParticipants(mSelected);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }

}