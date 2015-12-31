package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.content.Context;
import android.util.Log;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitGroupService;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.GroupRepository;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class RetrofitGroupRepository implements GroupRepository {

    final private String TAG = "GroupRepository";
    RetrofitGroupService mAPI;
    private Context mContext = null;

    public RetrofitGroupRepository(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitGroupService.class);
    }

    public void getOverView(final int groupId,
                            final BaseCallback<Group> cb) {
        mAPI.getOverView(groupId, new Callback<Group>() {
            @Override
            public void success(Group group, Response response) {
                cb.onSuccess(group);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    Log.e(TAG, error.getResponse().getStatus() + " " + error.getMessage());
                } else {
                    Log.e(TAG, error.getMessage());
                }
                cb.onFailure();
            }
        });

    }

    @Override
    public void get(int id, BaseCallback<Payment> cb) {

    }

    @Override
    public void getAll(final int userId,
                       final BaseCallback<List<Group>> cb) {
        mAPI.getAll(userId, new Callback<List<Group>>() {

            @Override
            public void success(List<Group> groups, Response response) {
                cb.onSuccess(groups);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    Log.e(TAG, error.getResponse().getStatus() + " " + error.getMessage());
                } else {
                    Log.e(TAG, error.getMessage());
                }
                cb.onFailure();
            }
        });
    }

    @Override
    public void getAll(BaseCallback<List<Payment>> cb) {

    }

    @Override
    public void create(Payment payment, BaseCallback<Payment> cb) {

    }

    @Override
    public void update(Payment payment, BaseCallback<Payment> cb) {

    }

    @Override
    public void delete(int id, BaseCallback<Payment> cb) {

    }
}
