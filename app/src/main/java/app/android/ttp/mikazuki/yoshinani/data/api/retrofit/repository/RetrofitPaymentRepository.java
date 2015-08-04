package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitPaymentService;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor.BaseRequestInterceptor;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.PaymentRepository;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;


/**
 * Created by haijimakazuki on 15/07/09.
 */
public class RetrofitPaymentRepository implements PaymentRepository {

    final private String TAG = "PaymentRepository";

    private Context mContext = null;
    RetrofitPaymentService mAPI;

    public RetrofitPaymentRepository(Context context) {
        this.mContext = context;
        buildAPI();
    }

    private void buildAPI() {
        Gson GSON = new GsonBuilder().create();

        RestAdapter REST_ADAPTER = new RestAdapter.Builder()
                .setEndpoint(ApiUtil.API_URL)
                .setConverter(new GsonConverter(GSON))
                .setRequestInterceptor(new BaseRequestInterceptor(mContext))
                .build();
        mAPI = REST_ADAPTER.create(RetrofitPaymentService.class);
    }

    @Override
    public void get(int id, BaseCallback<Payment> cb) {

    }

    @Override
    public void getAll(final BaseCallback<List<Payment>> cb) {
        mAPI.getPaymentsByUserId(new Callback<List<Payment>>() {
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
        mAPI.createNewPayment(payment.getAmount(), payment.getEvent(), payment.getDescription(), payment.getDate(), ApiUtil.GROUP_ID, payment.getPaidUserId(), payment.getParticipantsIds(), new Callback<Payment>() {
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
