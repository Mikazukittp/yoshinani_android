package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.view.activity.EditProfileActivity;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author haijimakazuki
 */
public class AccountSettingFragment extends Fragment {

    UserService mUserService;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_setting, container, false);
        ButterKnife.bind(this, view);
        mUserService = new UserService(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.menu_account));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.edit_profile)
    public void editProfile(View v) {
        Intent i = new Intent(getActivity(), EditProfileActivity.class);
        i.putExtras(getArguments());
        startActivity(i);
    }

    @OnClick(R.id.logout)
    public void logout(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("本当にログアウトしますか？")
                .setPositiveButton("はい", (dialog, id) -> {
//                    mUserService.deleteToken(PreferenceUtil.getNotificationToken(getActivity().getApplicationContext()));
                    PreferenceUtil.clearUserData(getActivity().getApplicationContext());
                    EventBus.getDefault().post(new UnauthorizedEvent());
                })
                .setNegativeButton("いいえ", (dialog, id) -> {
                });
        builder.create().show();
    }

}
