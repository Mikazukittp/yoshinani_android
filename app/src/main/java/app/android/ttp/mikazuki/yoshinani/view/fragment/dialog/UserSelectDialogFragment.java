package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.UserSelectEvent;

public class UserSelectDialogFragment extends DialogFragment {

    private int mSelected;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 受け取った変数の初期化・整形
        CharSequence[] items = getArguments().getCharSequenceArray("users");
        if (items == null) {
            items = new CharSequence[0];
        }
        mSelected = getArguments().getInt("selected", -1);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("返済相手")
                .setSingleChoiceItems(items, mSelected, (DialogInterface dialog, int which) -> {
                    mSelected = which;
                })
                .setPositiveButton(R.string.ok, (DialogInterface dialog, int id) -> {
                    EventBus.getDefault().post(new UserSelectEvent(mSelected));
                })
                .setNegativeButton(R.string.cancel, (DialogInterface dialog, int id) -> {
                });

        return builder.create();
    }

}