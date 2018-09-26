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
class EditProfileViewModel(private val mContext: Context,
                           user: UserModel) : BaseObservable(), Subscription {

    private val mId: Int
    val account: String
    var username = BindableString()
    var email = BindableString()

    private val mUserService: UserService

    private val compositeSubscription = CompositeSubscription()

    val onClick: View.OnClickListener
        get() = { v ->
            mUserService
                    .update(mId, username.get(), email.get())
                    .subscribe { response ->
                        if (response.isSuccess) {
                            EventBus.getDefault().post(FetchDataEvent(UserModel.from(response.body())))
                        } else {
                            EventBus.getDefault().post(ErrorEvent("プロフィール編集失敗", ApiUtil.getApiError(response).message))
                        }
                    }
            ViewUtils.hideKeyboard(mContext)
        }

    init {
        mId = user.id
        account = user.account
        username.set(user.username)
        email.set(user.email)

        mUserService = UserService(mContext)
    }

    override fun unsubscribe() {
        compositeSubscription.unsubscribe()
    }

    override fun isUnsubscribed(): Boolean {
        return false
    }

    companion object {

        private val TAG = EditProfileViewModel::class.java!!.getName()
    }

}
