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
public class ChangePasswordViewModel extends BaseObservable implements Subscription {

    private static final String TAG = ChangePasswordViewModel.class.getName();
    private final Context mContext;
    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    private BindableString mPassword = new BindableString();
    private BindableString mNewPassword = new BindableString();
    private BindableString mNewPasswordConfirm = new BindableString();

    private UserService mUserService;

    public ChangePasswordViewModel(Context context) {
        mContext = context;
        mUserService = new UserService(context);
    }

    public BindableString getPassword() {
        return mPassword;
    }

    public void setPassword(final String password) {
        this.mPassword.set(password);
    }

    public BindableString getNewPassword() {
        return mNewPassword;
    }

    public void setNewPassword(final String newPassword) {
        this.mNewPassword.set(newPassword);
    }

    public BindableString getNewPasswordConfirm() {
        return mNewPasswordConfirm;
    }

    public void setNewPasswordConfirm(final String newPasswordConfirm) {
        this.mNewPasswordConfirm.set(newPasswordConfirm);
    }

    public View.OnClickListener getOnClick() {
        return v -> {
            mUserService
                    .changePassword(mPassword.get(), mNewPassword.get(), mNewPasswordConfirm.get())
                    .subscribe(response -> {
                        if (response.isSuccess()) {
                            EventBus.getDefault().post(new FetchDataEvent<>(UserModel.from(response.body())));
                        } else {
                            EventBus.getDefault().post(new ErrorEvent("パスワード変更失敗", ApiUtil.getApiError(response).getMessage()));
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
