package app.android.ttp.mikazuki.yoshinani.view.activity

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.View
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.databinding.ActivityChangePasswordBinding
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.viewModel.ChangePasswordViewModel
import butterknife.BindView
import butterknife.ButterKnife
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

/**
 * @author haijimakazuki
 */
class ChangePasswordActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    internal var mToolbar: Toolbar? = null

    private var mBinding: ActivityChangePasswordBinding? = null
    private var mViewModel: ChangePasswordViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_change_password)
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

        mViewModel = ChangePasswordViewModel(applicationContext)
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

    @Subscribe
    fun onEvent(event: FetchDataEvent<UserModel>) {
        Snackbar.make(findViewById<View>(R.id.container), "変更完了", Snackbar.LENGTH_SHORT).show()
    }
}
