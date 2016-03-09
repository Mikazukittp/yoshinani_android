package app.android.ttp.mikazuki.yoshinani.viewModel;

import android.databinding.BaseObservable;

import app.android.ttp.mikazuki.yoshinani.binding.BindableString;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class EditProfileViewModel extends BaseObservable implements Subscription {

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private String mAccount;
    private BindableString mUsername = new BindableString();
    private BindableString mEmail = new BindableString();

    public EditProfileViewModel(UserModel user) {
        mAccount = user.getAccount();
        mUsername.set(user.getUsername());
        mEmail.set(user.getEmail());
    }

    public String getAccount() {
        return mAccount;
    }

    public void setAccount(final String account) {
        this.mAccount = account;
    }

    public BindableString getUsername() {
        return mUsername;
    }

    public void setUsername(final BindableString username) {
        this.mUsername = username;
    }

    public BindableString getEmail() {
        return mEmail;
    }

    public void setEmail(final BindableString email) {
        this.mEmail = email;
    }

    @Override
    public void unsubscribe() {
        compositeSubscription.unsubscribe();
    }

    @Override
    public boolean isUnsubscribed() {
        return false;
    }

}
