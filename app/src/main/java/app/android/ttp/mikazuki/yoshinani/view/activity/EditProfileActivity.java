package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.ActivityEditProfileBinding;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.viewModel.EditProfileViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @author haijimakazuki
 */
public class EditProfileActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private ActivityEditProfileBinding mBinding;
    private EditProfileViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile);
        ButterKnife.bind(this);

        // Toolbarの設定
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolbar.setTitle("プロフィール編集");

        mViewModel = new EditProfileViewModel(getApplicationContext(), Parcels.unwrap(getIntent().getParcelableExtra("me")));
        mBinding.setViewModel(mViewModel);
    }

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @OnClick(R.id.change_password)
    public void changePassword(View v) {
        goTo(ChangePasswordActivity.class);
    }

    @Subscribe
    public void onEvent(FetchDataEvent<UserModel> event) {
        Snackbar.make(findViewById(R.id.container), "変更完了", Snackbar.LENGTH_SHORT).show();
    }
}
