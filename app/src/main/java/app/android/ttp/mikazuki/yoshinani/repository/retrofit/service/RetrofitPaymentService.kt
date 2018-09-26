package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service


import java.util.ArrayList

import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Payment
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * @author haijimakazuki
 */
interface RetrofitPaymentService {

    @GET(PATH_PAYMENTS)
    fun getPayments(@Query("group_id") goupId: Int): Observable<Response<List<Payment>>>

    @GET(PATH_PAYMENTS)
    fun getNextPayments(@Query("group_id") goupId: Int, @Query("last_id") last_id: Int): Observable<Response<List<Payment>>>

    @FormUrlEncoded
    @GET(PATH_PAYMENT_WITH_ID)
    fun getPaymentById(@Path("id") payment_id: Int): Observable<Response<Payment>>

    @POST(PATH_PAYMENTS)
    fun createNewPayment(@Body data: RequestWrapper): Observable<Response<Payment>>

    @DELETE(PATH_PAYMENT_WITH_ID)
    fun deletePayment(@Path("id") payment_id: Int): Observable<Response<Payment>>

    class RequestWrapper(payment: PaymentModel) {
        var payment: PostData

        init {
            this.payment = PostData(payment.createEntity())
        }

        internal inner class PostData {
            var amount: Int = 0
            var groupId: Int = 0
            var event: String? = null
            var description: String? = null
            var date: String? = null
            var paidUserId: Int = 0
            var participantsIds: MutableList<Int>
            var isRepayment: Boolean = false

            constructor() {}

            constructor(payment: Payment) {
                amount = payment.amount
                groupId = payment.groupId
                event = payment.event
                description = payment.description

                date = payment.date
                paidUserId = payment.paidUser!!.id
                participantsIds = ArrayList()
                for (participant in payment.participants!!) {
                    participantsIds.add(participant.id)
                }
                isRepayment = payment.isRepayment
            }
        }

    }

    companion object {

        val PATH_PAYMENTS = "payments"
        val PATH_PAYMENT_WITH_ID = "payments/{id}"
    }
}
