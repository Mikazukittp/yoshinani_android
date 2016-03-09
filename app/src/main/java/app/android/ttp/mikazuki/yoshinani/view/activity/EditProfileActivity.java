package app.android.ttp.mikazuki.yoshinani.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.ActivityEditProfileBinding;
import app.android.ttp.mikazuki.yoshinani.viewModel.EditProfileViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class EditProfileActivity extends BaseActivity {

    @Bind(R.id.toolbar)
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

        mViewModel = new EditProfileViewModel(Parcels.unwrap(getIntent().getParcelableExtra("me")));
        mBinding.setViewModel(mViewModel);
    }

}
