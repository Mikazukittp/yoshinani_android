package app.android.ttp.mikazuki.yoshinani.viewModel

import android.content.Context
import android.databinding.BaseObservable
import android.databinding.Bindable

import java.util.ArrayList
import java.util.Calendar

import app.android.ttp.mikazuki.yoshinani.binding.BindableString
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.utils.ModelUtils
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * @author haijimakazuki
 */
class PostRepaymentViewModel(private val mContext: Context, private val mGroupId: Int) : BaseObservable(), Subscription {
    private var mAllUserModels: List<UserModel> = ArrayList()

    private val compositeSubscription = CompositeSubscription()

    val amount = BindableString()
    private var mDate: Calendar? = null
    var participantsId = -1
        private set
    private val isPostEnabled: Boolean = false

    var date: Calendar?
        @Bindable
        get() = mDate
        set(date) {
            this.mDate = date
            notifyChange()
        }

    val participants: String?
        @Bindable
        get() = if (participantsId == -1) {
            "返済相手選択"
        } else mAllUserModels[participantsId].displayName

    val isParticipantsEnabled: Boolean
        @Bindable
        get() = !mAllUserModels.isEmpty()

    val model: PaymentModel
        get() {
            val model = PaymentModel()
            model.id = -1
            model.groupId = mGroupId
            val uid = Integer.parseInt(PreferenceUtil.getUid(mContext)!!)
            val me = UserModel()
            me.id = uid
            model.paidUser = me
            model.setIsRepayment(true)

            model.setAmount(Integer.parseInt(amount.get()))
            model.setEvent("")
            model.setDescription("")
            model.date = mDate
            val participants = ArrayList<UserModel>()
            participants.add(mAllUserModels[participantsId])
            model.participants = participants
            return model
        }

    init {
        reset()
    }

    fun setAllUsers(allUsers: List<UserModel>) {
        mAllUserModels = allUsers
        notifyChange()
    }

    fun setAmount(amount: String?) {
        if (amount == null || TextUtils.unwrapCurrency(amount) != null) {
            this.amount.set(amount)
        }
    }

    fun setParticipants(participants: Int) {
        this.participantsId = participants
        notifyChange()
    }

    fun reset() {
        amount.set(null)
        mDate = ModelUtils.today
        participantsId = -1
    }

    override fun unsubscribe() {
        compositeSubscription.unsubscribe()
    }

    override fun isUnsubscribed(): Boolean {
        return false
    }
}
