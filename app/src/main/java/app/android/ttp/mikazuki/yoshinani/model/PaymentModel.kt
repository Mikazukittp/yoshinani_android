package app.android.ttp.mikazuki.yoshinani.model

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable

import org.parceler.Parcel

import java.util.Calendar
import java.util.Objects

import app.android.ttp.mikazuki.yoshinani.BR
import app.android.ttp.mikazuki.yoshinani.binding.BindableBoolean
import app.android.ttp.mikazuki.yoshinani.binding.BindableInt
import app.android.ttp.mikazuki.yoshinani.binding.BindableString
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Payment
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils
import rx.Observable

/**
 * @author haijimakazuki
 */
@Parcel
class PaymentModel : BaseObservable {

    var id: Int = 0
    var amount = BindableInt()
        internal set
    var event = BindableString()
        internal set
    var description = BindableString()
        internal set
    internal var date = ModelUtils.today
    var paidUser: UserModel
    var participants: List<UserModel>
    var groupId: Int = 0
    var isRepayment = BindableBoolean()
        internal set

    constructor() {}

    constructor(id: Int, amount: Int, event: String, description: String, date: Calendar, paidUser: UserModel, participants: List<UserModel>, groupId: Int, isRepayment: Boolean) {
        this.id = id
        this.amount.set(amount)
        this.event.set(event)
        this.description.set(description)
        this.date = date
        this.paidUser = paidUser
        this.participants = participants
        this.groupId = groupId
        this.isRepayment.set(isRepayment)
    }

    fun createEntity(): Payment {
        return Payment(
                this.id,
                this.amount.get(),
                this.event.get(),
                this.description.get(),
                ModelUtils.formatDate(this.date),
                this.paidUser.createEntity(),
                Observable.from(this.participants).map<User> { p -> p.createEntity() }.toList().toBlocking().single(),
                this.groupId,
                this.isRepayment.get()
        )
    }

    fun reset() {
        this.id = -1
        this.amount.set(0)
        this.event.set("")
        this.description.set("")
        //        this.date = new GregorianCalendar();
    }

    fun paid(context: Context): Boolean {
        return paidUser.id.toString() == PreferenceUtil.getUid(context)
    }

    fun participate(context: Context): Boolean {
        return Observable.from(participants)
                .map<Int>(Func1<UserModel, Int> { it.getId() })
                .map<String>(Func1<Int, String> { it.toString() })
                .contains(PreferenceUtil.getUid(context))
                .toBlocking().single()
    }

    fun setAmount(amount: Int) {
        this.amount.set(amount)
    }

    fun setEvent(event: String) {
        this.event.set(event)
    }

    fun setDescription(description: String) {
        this.description.set(description)
    }

    @Bindable
    fun getDate(): Calendar {
        return date
    }

    fun setDate(date: Calendar) {
        this.date = date
        notifyPropertyChanged(BR.date)
    }

    fun setIsRepayment(isRepayment: Boolean) {
        this.isRepayment.set(isRepayment)
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as PaymentModel?
        return id == that!!.id
    }

    override fun hashCode(): Int {
        return id
    }

    companion object {

        fun from(entity: Payment): PaymentModel {

            return PaymentModel(
                    entity.id,
                    entity.amount,
                    entity.event,
                    entity.description,
                    ModelUtils.parseDate(entity.date),
                    UserModel.from(entity.paidUser),
                    Observable.from<User>(entity.participants!!).map<UserModel> { u -> UserModel.from(u) }.toList().toBlocking().single(),
                    entity.groupId,
                    entity.isRepayment
            )
        }

        fun from(entities: List<Payment>): List<PaymentModel> {
            return Observable
                    .from(entities)
                    .map { p -> PaymentModel.from(p) }
                    .toList()
                    .toBlocking()
                    .single()
        }
    }
}
