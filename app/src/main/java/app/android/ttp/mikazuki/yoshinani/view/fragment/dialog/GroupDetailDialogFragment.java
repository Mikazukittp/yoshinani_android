package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.DialogGroupDetailBinding;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.services.GroupService;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class GroupDetailDialogFragment extends DialogFragment {

    @Bind(R.id.name)
    EditText mName;
    @Bind(R.id.description)
    EditText mDescription;

    private GroupModel mGroupModel;
    private GroupService mGroupService;
    private DialogGroupDetailBinding mBinding;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_group_detail, null, false);
        ButterKnife.bind(this, view);

        mGroupService = new GroupService(getActivity().getApplicationContext());

        String title;
        String positiveLabel;
        if (getArguments() != null) {
            // 編集
            mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));
            title = "グループ編集";
            positiveLabel = "編集";

        } else {
            // 新規作成
            mGroupModel = new GroupModel();
            title = "グループ新規作成";
            positiveLabel = "作成";
        }
        mBinding = DialogGroupDetailBinding.bind(view);
        mBinding.setGroup(mGroupModel);
        mGroupService = new GroupService(getActivity().getApplicationContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveLabel, (dialog, which) -> {
                    if (getArguments() != null) {
                        mGroupService.update(mGroupModel);
                    } else {
                        mGroupService.create(mGroupModel);
                    }
                })
                .setNegativeButton("キャンセル", (dialog, which) -> dismiss());
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
    public void onEvent(@NonNull final FetchDataEvent<GroupModel> event) {
        EventBus.getDefault().post(new RefreshEvent());
        dismiss();
    }

}