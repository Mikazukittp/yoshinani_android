package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.GroupUserModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.services.GroupService;

/**
 * @author haijimakazuki
 */
public class AcceptDialogFragment extends DialogFragment {

    private GroupService mGroupService;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 受け取った変数の初期化・整形
        GroupModel invitedGroup = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));
        mGroupService = new GroupService(getActivity().getApplicationContext());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(String.format("%s への招待", invitedGroup.getName().get()))
                .setMessage(invitedGroup.getDescription().get())
                .setPositiveButton("承認", (dialog, id) -> mGroupService.accept(invitedGroup.getId()))
                .setNeutralButton("閉じる", (dialog, id) -> {
                })
                .setNegativeButton("拒否", (dialog, id) -> {
                });

        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(FetchDataEvent<GroupUserModel> event) {
        EventBus.getDefault().post(new RefreshEvent());
        dismiss();
    }

}