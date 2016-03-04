package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.google.common.collect.Lists;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.UserMultiSelectEvent;

public class UserMultiSelectDialogFragment extends DialogFragment {

    private ArrayList<Integer> mSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 受け取った変数の初期化・整形
        CharSequence[] items = getArguments().getCharSequenceArray("users");
        if (items == null) {
            items = new CharSequence[0];
        }
        mSelected = getArguments().getIntegerArrayList("selected");
        if (mSelected == null) {
            mSelected = Lists.newArrayList();
        }

        boolean[] selectedFlags = new boolean[items.length];
        for (Integer i : mSelected) {
            selectedFlags[i] = true;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_participants_title)
                .setMultiChoiceItems(items, selectedFlags, (DialogInterface dialog, int which, boolean isChecked) -> {
                    if (isChecked) {
                        mSelected.add(which);
                    } else {
                        mSelected.remove(Integer.valueOf(which));
                    }
                })
                .setPositiveButton(R.string.ok, (DialogInterface dialog, int id) -> {
                    EventBus.getDefault().post(new UserMultiSelectEvent(mSelected));
                })
                .setNegativeButton(R.string.cancel, (DialogInterface dialog, int id) -> {
                });

        return builder.create();
    }

}