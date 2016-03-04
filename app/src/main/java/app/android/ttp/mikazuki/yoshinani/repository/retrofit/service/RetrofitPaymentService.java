package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service;

import com.google.common.collect.Lists;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author haijimakazuki
 */
public interface RetrofitPaymentService {

    static final String PATH_PAYMENTS = "payments";
    static final String PATH_PAYMENT_WITH_ID = "payments/{id}";

    @GET(PATH_PAYMENTS)
    public Observable<Response<List<Payment>>> getPayments(@Query("group_id") int goupId);

    @GET(PATH_PAYMENTS)
    public Observable<Response<List<Payment>>> getNextPayments(@Query("group_id") int goupId, @Query("last_id") int last_id);

    @FormUrlEncoded
    @GET(PATH_PAYMENT_WITH_ID)
    public Observable<Response<Payment>> getPaymentById(@Path("id") int payment_id);

    @POST(PATH_PAYMENTS)
    public Observable<Response<Payment>> createNewPayment(@Body RequestWrapper data);

    @DELETE(PATH_PAYMENT_WITH_ID)
    public Observable<Response<Payment>> deletePayment(@Path("id") int payment_id);

    public class RequestWrapper {
        public PostData payment;

        public RequestWrapper(PaymentModel payment) {
            this.payment = new PostData(payment.createEntity());
        }

        class PostData {
            public int amount;
            public int groupId;
            public String event;
            public String description;
            public String date;
            public int paidUserId;
            public List<Integer> participantsIds;
            public boolean isRepayment;

            public PostData() {
            }

            public PostData(Payment payment) {
                amount = payment.getAmount();
                groupId = payment.getGroupId();
                event = payment.getEvent();
                description = payment.getDescription();

                date = payment.getDate();
                paidUserId = payment.getPaidUser().getId();
                participantsIds = Lists.newArrayList();
                for (User participant : payment.getParticipants()) {
                    participantsIds.add(participant.getId());
                }
                isRepayment = payment.isRepayment();
            }
        }

    }
}
