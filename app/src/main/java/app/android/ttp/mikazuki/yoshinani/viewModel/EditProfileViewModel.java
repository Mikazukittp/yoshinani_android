package app.android.ttp.mikazuki.yoshinani.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.binding.BindableString;
import app.android.ttp.mikazuki.yoshinani.event.ErrorEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author haijimakazuki
 */
public class EditProfileViewModel extends BaseObservable implements Subscription {

    private static final String TAG = EditProfileViewModel.class.getName();

    private final int mId;
    private final String mAccount;
    private final Context mContext;
    private BindableString mUsername = new BindableString();
    private BindableString mEmail = new BindableString();

    private UserService mUserService;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    public EditProfileViewModel(Context context,
                                UserModel user) {
        mContext = context;
        mId = user.getId();
        mAccount = user.getAccount();
        mUsername.set(user.getUsername());
        mEmail.set(user.getEmail());

        mUserService = new UserService(context);
    }

    public String getAccount() {
        return mAccount;
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

    public View.OnClickListener getOnClick() {
        return v -> {
            mUserService
                    .update(mId, mUsername.get(), mEmail.get())
                    .subscribe(response -> {
                        if (response.isSuccess()) {
                            EventBus.getDefault().post(new FetchDataEvent<>(UserModel.from(response.body())));
                        } else {
                            EventBus.getDefault().post(new ErrorEvent("プロフィール編集失敗", ApiUtil.getApiError(response).getMessage()));
                        }
                    });
            ViewUtils.hideKeyboard(mContext);
        };
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
