package app.android.ttp.mikazuki.yoshinani.services;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitPaymentService;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author haijimakazuki
 */
public class PaymentService implements Subscription {

    final private String TAG = this.getClass().getSimpleName();
    private final EventBus eventbus = EventBus.getDefault();
    RetrofitPaymentService mAPI;
    private Context mContext = null;

    public PaymentService(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitPaymentService.class);
    }

    public void getAll(final int groupId) {
        mAPI.getPayments(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchListDataEvent<>(PaymentModel.from(response.body())).setTag("first"))
                        , throwable -> eventbus.post(new FetchListDataEvent<PaymentModel>(null)));
    }

    public void getNext(final int groupId, final int last_payment_id) {
        mAPI.getNextPayments(groupId, last_payment_id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchListDataEvent<>(PaymentModel.from(response.body())).setTag("next"))
                        , throwable -> eventbus.post(new FetchListDataEvent<PaymentModel>(null).setTag("next")));
    }

    public void create(final PaymentModel payment) {
        mAPI.createNewPayment(new RetrofitPaymentService.RequestWrapper(payment))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new RefreshEvent())
                        , throwable -> eventbus.post(new FetchDataEvent<PaymentModel>(null)));
    }

    public void delete(final int id) {
        mAPI.deletePayment(id)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(PaymentModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<PaymentModel>(null)));
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public boolean isUnsubscribed() {
        return false;
    }
}
