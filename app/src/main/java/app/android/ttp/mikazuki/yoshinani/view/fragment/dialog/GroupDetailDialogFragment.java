package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.DialogGroupDetailBinding;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.viewModel.GroupViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * @author haijimakazuki
 */
public class GroupDetailDialogFragment extends DialogFragment {

    @Bind(R.id.name)
    EditText mName;
    @Bind(R.id.description)
    EditText mDescription;

    private GroupModel mGroupModel;
    private GroupViewModel mGroupViewModel;
    private DialogGroupDetailBinding mBinding;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_group_detail, null, false);
        ButterKnife.bind(this, view);

        mGroupViewModel = new GroupViewModel(getActivity().getApplicationContext());

        String title;
        String positiveLabel;
        if (getArguments() != null) {
            // 編集
            mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));
            Log.d("!!!!!", mGroupModel.getName().get());
            Log.d("!!!!!", mGroupModel.getDescription().get());
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
        mGroupViewModel = new GroupViewModel(getActivity().getApplicationContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(view)
                .setPositiveButton(positiveLabel, (dialog, which) -> {
                    if (getArguments() != null) {
                        mGroupViewModel.update(mGroupModel);
                    } else {
                        mGroupViewModel.create(mGroupModel);
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
    public void onEvent(@NonNull final FetchDataEvent<GroupModel> event) {
        EventBus.getDefault().post(new RefreshEvent());
        dismiss();
    }

}