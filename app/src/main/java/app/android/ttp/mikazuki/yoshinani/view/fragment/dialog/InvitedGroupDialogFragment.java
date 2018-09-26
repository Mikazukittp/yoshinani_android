package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.GroupUserModel;
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.InvitedGroupListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * @author haijimakazuki
 */
public class InvitedGroupDialogFragment extends DialogFragment {

    @BindView(R.id.list_view)
    ListView mListView;

    private List<GroupModel> mInvitedGroups;
    private Bundle mBundle;

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // 受け取った変数の初期化・整形
        List<GroupModel> invitedGroups = new ArrayList<>();
        final ArrayList<Parcelable> parcelables = getArguments().getParcelableArrayList("invitedGroups");
        if (parcelables != null) {
            invitedGroups = Observable.from(parcelables)
                    .map(Parcels::unwrap)
                    .cast(GroupModel.class)
                    .toList()
                    .toBlocking().single();
        }

        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_invited_groups, null, false);
        ButterKnife.bind(this, view);
        mListView.setAdapter(new InvitedGroupListAdapter(getActivity().getApplicationContext(), invitedGroups));
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("お知らせ")
                .setView(view)
                .setNegativeButton("閉じる", (dialog, id) -> {
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