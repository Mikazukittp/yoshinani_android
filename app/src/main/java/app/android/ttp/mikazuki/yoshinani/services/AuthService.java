package app.android.ttp.mikazuki.yoshinani.services;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.ResponseMessage;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitUserService;
import app.android.ttp.mikazuki.yoshinani.view.activity.MainActivity;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author haijimakazuki
 */
public class AuthService implements Subscription {

    private final String tag = this.getClass().getName();
    RetrofitUserService mAPI;
    private Context mContext = null;

    public AuthService(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext, false)
                .create(RetrofitUserService.class);
    }

    public void signUp(@NonNull final String account,
                       @NonNull final String email,
                       @NonNull final String password) {
        mAPI.createUser(new RetrofitUserService.RequestWrapper(account, email, password))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    PreferenceUtil.putUserData(mContext, response.body());
                    EventBus.getDefault().post(new ActivityTransitionEvent(MainActivity.class, false));
                }, t -> {
                    t.printStackTrace();
                    Log.e(tag, t.getMessage());
                    EventBus.getDefault().post(new ShowSnackbarEvent("新規登録失敗"));
                });
    }

    public void signIn(final String account,
                       final String password) {
        mAPI.getAuthToken(account, password)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    PreferenceUtil.putUserData(mContext, response.body());
                    EventBus.getDefault().post(new ActivityTransitionEvent(MainActivity.class, false));
                }, t -> {
                    t.printStackTrace();
                    Log.e(tag, t.getMessage());
                    EventBus.getDefault().post(new ShowSnackbarEvent("ログイン失敗"));
                });
    }

    public Observable<Response<ResponseMessage>> forgetPassword(final String email) {
        return mAPI.forgetPassword(new RetrofitUserService.ForgetRequestWrapper(email))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<User>> resetPassword(final String token,
                                                    final String newPassword,
                                                    final String newPasswordConfirmation) {
        return mAPI.resetPassword(new RetrofitUserService.ResetPasswordRequestWrapper(token, newPassword, newPasswordConfirmation))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Response<User>> signInWithOAuth(@NonNull final String id,
                                                      @NonNull final int whichSNS,
                                                      @NonNull final String hashedId) {
        return mAPI.signInWithOAuth(new RetrofitUserService.OAuthRequestWrapper(id, whichSNS, hashedId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void unsubscribe() {
//        messenger.unsubscribe();
    }

    @Override
    public boolean isUnsubscribed() {
        return false;
//        return messenger.isUnsubscribed();
    }
}