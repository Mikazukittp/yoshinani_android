package app.android.ttp.mikazuki.yoshinani.viewModel;

import android.content.Context;

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitPaymentService;
import de.greenrobot.event.EventBus;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author haijimakazuki
 */
public class PaymentViewModel implements Subscription {

    final private String TAG = this.getClass().getSimpleName();
    private final EventBus eventbus = EventBus.getDefault();
    RetrofitPaymentService mAPI;
    private Context mContext = null;

    public PaymentViewModel(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitPaymentService.class);
    }

    public void getAll(final int groupId) {
        mAPI.getPayments(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchListDataEvent<>(PaymentModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchListDataEvent<PaymentModel>(null)));
    }

    public void create(final PaymentModel payment) {
        mAPI.createNewPayment(new RetrofitPaymentService.RequestWrapper(payment))
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
