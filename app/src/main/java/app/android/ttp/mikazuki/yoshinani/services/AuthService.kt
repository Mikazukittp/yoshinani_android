package app.android.ttp.mikazuki.yoshinani.services

import android.content.Context
import android.util.Log
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.ResponseMessage
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitUserService
import app.android.ttp.mikazuki.yoshinani.view.activity.MainActivity
import org.greenrobot.eventbus.EventBus
import retrofit2.Response
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author haijimakazuki
 */
class AuthService(context: Context) : Subscription {

    private val tag = this.javaClass.getName()
    internal var mAPI: RetrofitUserService
    private val mContext: Context

    init {
        this.mContext = context
        mAPI = ApiUtil
                .buildRESTAdapter(mContext, false)
                .create(RetrofitUserService::class.java!!)
    }

    fun signUp(account: String,
               email: String,
               password: String) {
        mAPI.createUser(RetrofitUserService.RequestWrapper(account, email, password))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    PreferenceUtil.putUserData(mContext, response.body())
                    EventBus.getDefault().post(ActivityTransitionEvent(MainActivity::class.java, false))
                }, { t ->
                    t.printStackTrace()
                    Log.e(tag, t.message)
                    EventBus.getDefault().post(ShowSnackbarEvent("新規登録失敗"))
                })
    }

    fun signIn(account: String,
               password: String) {
        mAPI.getAuthToken(account, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    PreferenceUtil.putUserData(mContext, response.body())
                    EventBus.getDefault().post(ActivityTransitionEvent(MainActivity::class.java, false))
                }, { t ->
                    t.printStackTrace()
                    Log.e(tag, t.message)
                    EventBus.getDefault().post(ShowSnackbarEvent("ログイン失敗"))
                })
    }

    fun forgetPassword(email: String): Observable<Response<ResponseMessage>> {
        return mAPI.forgetPassword(RetrofitUserService.ForgetRequestWrapper(email))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun resetPassword(token: String,
                      newPassword: String,
                      newPasswordConfirmation: String): Observable<Response<User>> {
        return mAPI.resetPassword(RetrofitUserService.ResetPasswordRequestWrapper(token, newPassword, newPasswordConfirmation))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun signInWithOAuth(id: String,
                        whichSNS: Int,
                        hashedId: String): Observable<Response<User>> {
        return mAPI.signInWithOAuth(RetrofitUserService.OAuthRequestWrapper(id, whichSNS, hashedId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun unsubscribe() {
        //        messenger.unsubscribe();
    }

    override fun isUnsubscribed(): Boolean {
        return false
        //        return messenger.isUnsubscribed();
    }
}