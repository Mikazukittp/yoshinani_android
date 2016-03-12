package app.android.ttp.mikazuki.yoshinani.viewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import butterknife.ButterKnife;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class SignInViewModel extends BaseObservable implements Subscription {

    private CompositeSubscription subscription;

    public SignInViewModel(View view) {
        ButterKnife.bind(this, view);
    }

    @Bindable
    public boolean getButtonEnabled() {
        return true;
    }

    @Override
    public void unsubscribe() {
        subscription.unsubscribe();
    }

    @Override
    public boolean isUnsubscribed() {
        return false;
    }
}
