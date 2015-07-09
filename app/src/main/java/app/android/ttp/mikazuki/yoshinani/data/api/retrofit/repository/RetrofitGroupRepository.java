package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitGroupService;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitPaymentService;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor.BaseRequestInterceptor;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor.QuestionRequestInterceptor;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.GroupRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.PaymentRepository;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class RetrofitGroupRepository implements GroupRepository {

    final private String TAG = "GroupRepository";

    private Context mContext = null;
    private String groupId = "559eaa98d430387816a640b1";
    RetrofitGroupService mAPI;

    public RetrofitGroupRepository(Context context) {
        this.mContext = context;
        buildAPI();
    }

    private void buildAPI() {
        Gson GSON = new GsonBuilder().create();

        RestAdapter REST_ADAPTER = new RestAdapter.Builder()
                .setEndpoint(ApiUtil.API_URL)
                .setConverter(new GsonConverter(GSON))
                .setRequestInterceptor(new BaseRequestInterceptor(mContext))
                .build();
        mAPI = REST_ADAPTER.create(RetrofitGroupService.class);
    }

    public void getOverView(final BaseCallback<Group> cb) {
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
