package app.android.ttp.mikazuki.yoshinani.services

import android.content.Context

import org.greenrobot.eventbus.EventBus

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitPaymentService
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * @author haijimakazuki
 */
class PaymentService(context: Context) : Subscription {

    private val TAG = this.javaClass.getSimpleName()
    private val eventbus = EventBus.getDefault()
    internal var mAPI: RetrofitPaymentService
    private val mContext: Context? = null

    init {
        this.mContext = context
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitPaymentService::class.java!!)
    }

    fun getAll(groupId: Int) {
        mAPI.getPayments(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchListDataEvent(PaymentModel.from(response.body())).setTag("first")) }, { throwable -> eventbus.post(FetchListDataEvent<PaymentModel>(null!!)) })
    }

    fun getNext(groupId: Int, last_payment_id: Int) {
        mAPI.getNextPayments(groupId, last_payment_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchListDataEvent(PaymentModel.from(response.body())).setTag("next")) }, { throwable -> eventbus.post(FetchListDataEvent<PaymentModel>(null!!).tag = "next") })
    }

    fun create(payment: PaymentModel) {
        mAPI.createNewPayment(RetrofitPaymentService.RequestWrapper(payment))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(RefreshEvent()) }, { throwable -> eventbus.post(FetchDataEvent<PaymentModel>(null)) })
    }

    fun delete(id: Int) {
        mAPI.deletePayment(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response -> eventbus.post(FetchDataEvent(PaymentModel.from(response.body()))) }, { throwable -> eventbus.post(FetchDataEvent<PaymentModel>(null)) })
    }

    override fun unsubscribe() {

    }

    override fun isUnsubscribed(): Boolean {
        return false
    }
}
