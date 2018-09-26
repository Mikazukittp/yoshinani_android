package app.android.ttp.mikazuki.yoshinani.services

import android.content.Context

import org.greenrobot.eventbus.EventBus

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.GroupUserModel
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitGroupService
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author haijimakazuki
 */
class GroupService(context: Context) : Subscription {

    private val TAG = this.javaClass.getSimpleName()
    private val eventbus = EventBus.getDefault()
    internal var mAPI: RetrofitGroupService
    private val mContext: Context

    init {
        this.mContext = context
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitGroupService::class.java!!)
    }

    fun getAll(userId: Int) {
        mAPI.getAll(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchListDataEvent(GroupModel.from(response.body())!!)) }, { throwable -> eventbus.post(FetchListDataEvent<Any>(null!!)) })
    }

    operator fun get(groupId: Int) {
        mAPI.get(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchDataEvent(GroupModel.from(response.body()))) }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    fun create(group: GroupModel) {
        mAPI.create(RetrofitGroupService.RequestDataOnCreate(group))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(RefreshEvent()) }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    fun update(group: GroupModel) {
        mAPI.update(group.id, RetrofitGroupService.RequestDataOnCreate(group))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchDataEvent(GroupModel.from(response.body()))) }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    fun invite(groupId: Int, userId: Int) {
        mAPI.invite(groupId, RetrofitGroupService.RequestDataOnInvite(userId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchDataEvent(GroupModel.from(response.body()))) }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    fun accept(groupId: Int) {
        mAPI.accept(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchDataEvent(GroupUserModel.from(response.body()))) }, { throwable -> eventbus.post(FetchDataEvent<Any>(null)) })
    }

    override fun unsubscribe() {

    }

    override fun isUnsubscribed(): Boolean {
        return false
    }
}
