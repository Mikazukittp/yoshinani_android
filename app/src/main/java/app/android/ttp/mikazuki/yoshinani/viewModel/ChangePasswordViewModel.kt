package app.android.ttp.mikazuki.yoshinani.viewModel

import android.content.Context
import android.databinding.BaseObservable
import android.view.View

import org.greenrobot.eventbus.EventBus

import app.android.ttp.mikazuki.yoshinani.binding.BindableString
import app.android.ttp.mikazuki.yoshinani.event.ErrorEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * @author haijimakazuki
 */
class ChangePasswordViewModel(private val mContext: Context) : BaseObservable(), Subscription {
    private val compositeSubscription = CompositeSubscription()

    val password = BindableString()
    val newPassword = BindableString()
    val newPasswordConfirm = BindableString()

    private val mUserService: UserService

    val onClick: View.OnClickListener
        get() = { v ->
            mUserService
                    .changePassword(password.get(), newPassword.get(), newPasswordConfirm.get())
                    .subscribe { response ->
                        if (response.isSuccess) {
                            EventBus.getDefault().post(FetchDataEvent(UserModel.from(response.body())))
                        } else {
                            EventBus.getDefault().post(ErrorEvent("パスワード変更失敗", ApiUtil.getApiError(response).message))
                        }
                    }
            ViewUtils.hideKeyboard(mContext)
        }

    init {
        mUserService = UserService(mContext)
    }

    fun setPassword(password: String) {
        this.password.set(password)
    }

    fun setNewPassword(newPassword: String) {
        this.newPassword.set(newPassword)
    }

    fun setNewPasswordConfirm(newPasswordConfirm: String) {
        this.newPasswordConfirm.set(newPasswordConfirm)
    }

    override fun unsubscribe() {
        compositeSubscription.unsubscribe()
    }

    override fun isUnsubscribed(): Boolean {
        return false
    }

    companion object {

        private val TAG = ChangePasswordViewModel::class.java!!.getName()
    }

}
