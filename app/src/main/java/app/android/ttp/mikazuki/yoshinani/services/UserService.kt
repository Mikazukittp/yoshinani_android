package app.android.ttp.mikazuki.yoshinani.services

import android.content.Context

import org.greenrobot.eventbus.EventBus

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitUserService
import retrofit2.Response
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class UserService(context: Context) : Subscription {

    private val TAG = this.javaClass.getSimpleName()
    private val eventbus = EventBus.getDefault()
    internal var mAPI: RetrofitUserService
    private val mContext: Context? = null

    init {
        this.mContext = context
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitUserService::class.java!!)
    }

    fun getAll(groupId: Int) {
        mAPI.getUsers(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchListDataEvent(UserModel.from(response.body())!!)) }, { throwable -> eventbus.post(FetchListDataEvent<Any>(null!!)) })
    }

    fun getMe() {
        mAPI.getMe(PreferenceUtil.getUid(mContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val model = UserModel.from(response.body())
                    eventbus.post(FetchDataEvent(model))
                }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    fun update(userId: Int, username: String, email: String): Observable<Response<User>> {
        return mAPI.updateUser(userId, RetrofitUserService.UpdateRequestWrapper(username, email))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun setAccountToOAuthUser(userId: Int, account: String): Observable<Response<User>> {
        return mAPI.registerAccount(userId, RetrofitUserService.UpdateAccountRequestWrapper(account))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun search(account: String) {
        mAPI.search(account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchDataEvent(UserModel.from(response.body()))) }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    fun changePassword(password: String,
                       newPassword: String,
                       newPasswordConfirmation: String): Observable<Response<User>> {
        return mAPI.changePassword(RetrofitUserService.ChangePasswordRequestWrapper(password, newPassword, newPasswordConfirmation))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun registerToken(token: String): Observable<Response<User>> {
        return mAPI.registerToken(RetrofitUserService.NotificationRequestWrapper(token, null))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateToken(token: String,
                    oldToken: String?): Observable<Response<User>> {
        return mAPI.updateToken(RetrofitUserService.NotificationRequestWrapper(token, oldToken))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteToken(token: String): Observable<Response<User>> {
        return mAPI.deleteToken(RetrofitUserService.NotificationRequestWrapper(token, null))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun unsubscribe() {

    }

    override fun isUnsubscribed(): Boolean {
        return false
    }
}
