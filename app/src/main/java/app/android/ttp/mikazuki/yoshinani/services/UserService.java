package app.android.ttp.mikazuki.yoshinani.services;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitUserService;
import retrofit2.Response;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserService implements Subscription {

    final private String TAG = this.getClass().getSimpleName();
    private final EventBus eventbus = EventBus.getDefault();
    RetrofitUserService mAPI;
    private Context mContext = null;

    public UserService(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitUserService.class);
    }

    public void getAll(final int groupId) {
        mAPI.getUsers(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchListDataEvent<>(UserModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchListDataEvent<>(null)));
    }

    public void getMe() {
        mAPI.getMe(PreferenceUtil.getUid(mContext))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                            UserModel model = UserModel.from(response.body());
                            eventbus.post(new FetchDataEvent<>(model));
                        }
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public Observable<Response<User>> update(final int userId, final String username, final String email) {
        return mAPI.updateUser(userId, new RetrofitUserService.UpdateRequestWrapper(username, email))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public void search(String account) {
        mAPI.search(account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(UserModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public Observable<Response<User>> changePassword(final String password,
                                                     final String newPassword,
                                                     final String newPasswordConfirmation) {
        return mAPI.changePassword(new RetrofitUserService.ChangePasswordRequestWrapper(password, newPassword, newPasswordConfirmation))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public boolean isUnsubscribed() {
        return false;
    }
}
