package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
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

    @FormUrlEncoded
    @POST(PATH_PAYMENTS)
    public void createNewPayment(@Field("amount") int amount,
                                 @Field("event") String event,
                                 @Field("description") String description,
                                 @Field("date") String date,
                                 @Field("groupId") String groupId,
                                 @Field("paidUserId") String paidUserId,
                                 @Field("participantsIds") List<String> participantsIds,
                                 Callback<Payment> cb);

}
