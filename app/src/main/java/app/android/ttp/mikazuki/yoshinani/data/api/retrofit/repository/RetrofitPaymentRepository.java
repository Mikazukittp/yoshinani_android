package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitPaymentService;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.PaymentRepository;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * Created by haijimakazuki on 15/07/09.
 */
public class RetrofitPaymentRepository implements PaymentRepository {

    final private String TAG = "PaymentRepository";

    private Context mContext = null;
    RetrofitPaymentService mAPI;

    public RetrofitPaymentRepository(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitPaymentService.class);

    }

    @Override
    public void get(int id, BaseCallback<Payment> cb) {

    }

    @Override
    public void getAll(BaseCallback<List<Payment>> cb) {

    }

    @Override
    public void getAll(int groupId, final BaseCallback<List<Payment>> cb) {
        mAPI.getPayments(groupId, new Callback<List<Payment>>() {
            @Override
            public void success(List<Payment> payments, Response response) {
                cb.onSuccess(payments);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    Log.e(TAG, error.getResponse().getStatus() + " " + error.getMessage());
                } else {
                    Log.e(TAG, error.getMessage());
                }
                cb.onFailure();
            }
        });
    }

    @Override
    public void create(Payment payment, final BaseCallback<Payment> cb) {
        List<Integer> participantIds = new ArrayList<Integer>();
        for (User participant : payment.getParticipants()) {
            participantIds.add(participant.getId());
        }
        mAPI.createNewPayment(new RetrofitPaymentService.RequestWrapper(payment), new Callback<Payment>() {
            @Override
            public void success(Payment payment, Response response) {
                cb.onSuccess(payment);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    Log.e(TAG, error.getResponse().getStatus() + " " + error.getMessage());
                } else {
                    Log.e(TAG, error.getMessage());
                }
                cb.onFailure();
            }
        });
    }

    @Override
    public void update(Payment payment, BaseCallback<Payment> cb) {

    }

    @Override
    public void delete(int id, BaseCallback<Payment> cb) {

    }
}
