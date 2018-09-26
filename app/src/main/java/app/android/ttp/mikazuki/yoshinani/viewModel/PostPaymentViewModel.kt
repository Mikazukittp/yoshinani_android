package app.android.ttp.mikazuki.yoshinani.viewModel

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable
import app.android.ttp.mikazuki.yoshinani.binding.BindableString
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils
import rx.Observable
import rx.Subscription
import rx.subscriptions.CompositeSubscription
import java.util.*

/**
 * @author haijimakazuki
 */
class PostPaymentViewModel(private val mContext: Context, private val mGroupId: Int) : BaseObservable(), Subscription {
    private var mAllUserModels: List<UserModel> = ArrayList()

    private val compositeSubscription = CompositeSubscription()

    val amount = BindableString()
    val event = BindableString()
    val description = BindableString()
    private var mDate: Calendar
    private var mParticipants: List<Int> = ArrayList()
    private val isParticipantsEnabled: Boolean = false
    private val isPostEnabled: Boolean = false

    var date: Calendar
        @Bindable
        get() = mDate
        set(date) {
            this.mDate = date
            notifyChange()
        }

    val participantsIdArray: ArrayList<Int>
        get() = mParticipants as ArrayList<Int>

    val participants: String
        @Bindable
        get() {
            val maxDisplaySize = 3

            if (mParticipants.isEmpty()) {
                return "参加者選択"
            }

            var names = Observable.from(mParticipants)
                    .limit(maxDisplaySize)
                    .map { id -> mAllUserModels[id!!].displayName }
                    .reduce<Any>(null) { ns, n -> if (ns == null) n else ns.toString() + "," + n }
                    .toBlocking().firstOrDefault("") as String
            if (mParticipants.size > maxDisplaySize) {
                names += " 他" + (mParticipants.size - maxDisplaySize) + "名"
            }
            return names
        }

    val model: PaymentModel
        get() {
            val model = PaymentModel()
            model.id = -1
            model.groupId = mGroupId
            val uid = Integer.parseInt(PreferenceUtil.getUid(mContext)!!)
            val me = UserModel()
            me.id = uid
            model.paidUser = me
            model.setIsRepayment(false)

            model.setAmount(Integer.parseInt(amount.get()))
            model.setEvent(event.get())
            model.setDescription(description.get())
            model.date = mDate
            val participants = ArrayList<UserModel>()
            for (i in mParticipants.indices) {
                participants.add(mAllUserModels[mParticipants[i]])
            }
            model.participants = participants
            return model
        }

    init {
        amount.set(null)
        event.set(null)
        description.set(null)
        mDate = ModelUtils.today
        mParticipants = ArrayList()
    }

    fun setAllUsers(allUsers: List<UserModel>) {
        mAllUserModels = allUsers
    }

    fun setAmount(amount: String?) {
        if (amount == null || TextUtils.unwrapCurrency(amount) != null) {
            this.amount.set(amount)
        }
    }

    fun setEvent(event: String) {
        this.event.set(event)
    }

    fun setDescription(description: String) {
        this.description.set(description)
    }

    fun setParticipants(participants: List<Int>) {
        this.mParticipants = participants
        notifyChange()
    }

    fun reset() {
        amount.set(null)
        event.set(null)
        description.set(null)
        mDate = ModelUtils.today
        mParticipants = ArrayList()
    }

    override fun unsubscribe() {
        compositeSubscription.unsubscribe()
    }

    override fun isUnsubscribed(): Boolean {
        return false
    }
}
