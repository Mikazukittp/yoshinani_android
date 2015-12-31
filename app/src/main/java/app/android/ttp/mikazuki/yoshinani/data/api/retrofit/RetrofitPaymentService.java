package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.ArrayList;
import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitPaymentService {

    static final String PATH_PAYMENTS = "/payments";
    static final String PATH_PAYMENT_WITH_ID = "/payments/{id}";

    // 未実装(引数などは適当なので変えてください)
    @GET(PATH_PAYMENTS)
    public void getPayments(@Query("group_id")int goupId, Callback<List<Payment>> cb);

    // 未実装(引数などは適当なので変えてください)
    @FormUrlEncoded
    @GET(PATH_PAYMENT_WITH_ID)
    public void getPaymentById(@Path("id") String payment_id, Callback<Payment> cb);

    @POST(PATH_PAYMENTS)
    public void createNewPayment(@Body RequestWrapper data, Callback<Payment> cb);



    public class RequestWrapper {
        public PostData payment;

        public RequestWrapper(Payment payment) {
            this.payment = new PostData(payment);
        }

        class PostData {
            public int amount;
            public int groupId;
            public String event;
            public String description;
            public String date;
            public int paidUserId;
            public List<Integer> participantsIds;

            public PostData() {
            }
            public PostData(Payment payment) {
                amount = payment.getAmount();
                groupId = 1;
                event = payment.getEvent();
                description = payment.getDescription();

//                date = payment.getDate();
                date = "2015-08-21";
                paidUserId = payment.getPaidUser().getId();
                participantsIds = new ArrayList<Integer>();
                for (User participant : payment.getParticipants()) {
                    participantsIds.add(participant.getId());
                }
            }
        }

    }
}
