package app.android.ttp.mikazuki.yoshinani.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.ActionBar
import android.support.v7.widget.Toolbar
import android.view.View

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.databinding.ActivityEditProfileBinding
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.viewModel.EditProfileViewModel
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick

/**
 * @author haijimakazuki
 */
class EditProfileActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null

    private var mBinding: ActivityEditProfileBinding? = null
    private var mViewModel: EditProfileViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        ButterKnife.bind(this)

        // Toolbarの設定
        setSupportActionBar(mToolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true)
            actionBar.setDisplayHomeAsUpEnabled(true)
        }
        mToolbar!!.setNavigationOnClickListener { v -> onBackPressed() }
        mToolbar!!.title = "プロフィール編集"

        mViewModel = EditProfileViewModel(applicationContext, Parcels.unwrap(intent.getParcelableExtra<Parcelable>("me")))
        mBinding!!.viewModel = mViewModel
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @OnClick(R.id.change_password)
    fun changePassword(v: View) {
        goTo(ChangePasswordActivity::class.java)
    }

    @Subscribe
    fun onEvent(event: FetchDataEvent<UserModel>) {
        Snackbar.make(findViewById(R.id.container), "変更完了", Snackbar.LENGTH_SHORT).show()
    }
}
