package app.android.ttp.mikazuki.yoshinani.services;

import android.content.Context;
import android.support.annotation.NonNull;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.GroupUserModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.service.RetrofitGroupService;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author haijimakazuki
 */
public class GroupService implements Subscription {

    final private String TAG = this.getClass().getSimpleName();
    private final EventBus eventbus = EventBus.getDefault();
    RetrofitGroupService mAPI;
    private Context mContext = null;

    public GroupService(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitGroupService.class);
    }

    public void getAll(final int userId) {
        mAPI.getAll(userId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchListDataEvent<>(GroupModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchListDataEvent<>(null)));
    }

    public void get(final int groupId) {
        mAPI.get(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(GroupModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public void create(@NonNull final GroupModel group) {
        mAPI.create(new RetrofitGroupService.RequestDataOnCreate(group))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new RefreshEvent())
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public void update(@NonNull final GroupModel group) {
        mAPI.update(group.getId(), new RetrofitGroupService.RequestDataOnCreate(group))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(GroupModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public void invite(int groupId, int userId) {
        mAPI.invite(groupId, new RetrofitGroupService.RequestDataOnInvite(userId))
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(GroupModel.from(response.body())))
                        , throwable -> eventbus.post(new FetchDataEvent<>(null)));
    }

    public void accept(int groupId) {
        mAPI.accept(groupId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> eventbus.post(new FetchDataEvent<>(GroupUserModel.from(response.body())))
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
