package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import retrofit.Callback;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitPaymentService {

    static final String PATH_PAYMENTS = "/api/payments";
    static final String PATH_PAYMENT_WITH_ID = "/api/payments/{id}";

    // 未実装(引数などは適当なので変えてください)
    @GET(PATH_PAYMENTS)
    public void getPaymentsByUserId(Callback<List<Payment>> cb);

    // 未実装(引数などは適当なので変えてください)
    @FormUrlEncoded
    @GET(PATH_PAYMENT_WITH_ID)
    public void getPaymentById(@Path("id") String payment_id, Callback<Payment> cb);


}
