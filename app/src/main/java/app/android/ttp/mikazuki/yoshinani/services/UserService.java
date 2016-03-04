package app.android.ttp.mikazuki.yoshinani.services;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitUserService;
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

    public void search(String account) {
        mAPI.search(account)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(UserModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public void changePassword(final String oldPassword,
                               final String newPassword,
                               final String newPasswordConfirmation) {
        mAPI.changePassword(oldPassword, newPassword, newPasswordConfirmation)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(UserModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    @Override
    public void unsubscribe() {

    }

    @Override
    public boolean isUnsubscribed() {
        return false;
    }
}
