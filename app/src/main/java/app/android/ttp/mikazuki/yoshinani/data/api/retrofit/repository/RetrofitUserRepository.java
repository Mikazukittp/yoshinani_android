package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.content.Context;
import android.util.Log;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitUserService;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class RetrofitUserRepository implements UserRepository {

    final private String TAG = "UserRepository";

    private Context mContext = null;
    RetrofitUserService mAPI;

    public RetrofitUserRepository(Context context) {
        this.mContext = context;
        mAPI = ApiUtil
                .buildRESTAdapter(mContext)
                .create(RetrofitUserService.class);
    }

    @Override
    public void get(int id, BaseCallback<User> cb) {

    }

    @Override
    public void getAll(BaseCallback<List<User>> cb) {

    }

    @Override
    public void getAll(int groupId, final BaseCallback<List<User>> cb) {
        mAPI.getUsers(groupId, new Callback<List<User>>() {
            @Override
            public void success(List<User> users, Response response) {
                cb.onSuccess(users);
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
    public void create(User user, BaseCallback<User> cb) {

    }

    @Override
    public void update(User user, BaseCallback<User> cb) {

    }

    @Override
    public void delete(int id, BaseCallback<User> cb) {

    }

    @Override
    public void signIn(String email, String password, final BaseCallback<User> cb) {
        mAPI.getAuthToken(email, password, new Callback<User>() {
            @Override
            public void success(User user, Response response) {
                cb.onSuccess(user);
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

}
