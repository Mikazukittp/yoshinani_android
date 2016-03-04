package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.services.GroupService;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author haijimakazuki
 */
public class UserSearchDialogFragment extends DialogFragment {

    @Bind(R.id.account)
    EditText mAccount;
    @Bind(R.id.search_result)
    LinearLayout mSearchResult;
    @Bind(R.id.found_account)
    TextView mFoundAccount;
    @Bind(R.id.found_username)
    TextView mFoundUsername;
    @Bind(R.id.search_btn)
    Button mSearchBtn;

    private GroupModel mGroup;
    private UserModel mUser;
    private UserService mUserService;
    private GroupService mGroupService;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_user_search, null, false);
        ButterKnife.bind(this, view);

        mGroup = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));

        mUserService = new UserService(getActivity().getApplicationContext());
        mGroupService = new GroupService(getActivity().getApplicationContext());
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setView(view)
                .setPositiveButton("招待", (dialog, which) -> {
                    if (mUser != null) {
                        mGroupService.invite(mGroup.getId(), mUser.getId());
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

    @OnClick(R.id.search_btn)
    public void searchUser(View view) {
        if (mAccount.getText().length() > 0) {
            mUserService.search(mAccount.getText().toString());
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(@NonNull final FetchDataEvent<UserModel> event) {
        mUser = event.getData();
        if (mSearchResult.getVisibility() == View.GONE) {
            mSearchResult.setVisibility(View.VISIBLE);
        }

        mFoundAccount.setText(mUser.getAccount());
        mFoundUsername.setText(mUser.getUsername());
    }

}