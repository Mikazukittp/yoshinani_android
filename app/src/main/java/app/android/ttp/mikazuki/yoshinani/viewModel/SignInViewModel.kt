package app.android.ttp.mikazuki.yoshinani.viewModel

import android.databinding.BaseObservable
import android.databinding.Bindable
import android.view.View

import butterknife.ButterKnife
import rx.Subscription
import rx.subscriptions.CompositeSubscription

/**
 * @author haijimakazuki
 */
class SignInViewModel(view: View) : BaseObservable(), Subscription {

    private val subscription: CompositeSubscription? = null

    val buttonEnabled: Boolean
        @Bindable
        get() = true

    init {
        ButterKnife.bind(this, view)
    }

    override fun unsubscribe() {
        subscription!!.unsubscribe()
    }

    override fun isUnsubscribed(): Boolean {
        return false
    }
}
