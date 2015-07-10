package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitAuthService;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor.AuthRequestInterceptor;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Token;
import app.android.ttp.mikazuki.yoshinani.domain.repository.AuthRepository;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class RetrofitAuthRepository implements AuthRepository {

    final private String TAG = "AuthRepository";

    Gson GSON = new GsonBuilder().create();

    RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(ApiUtil.API_URL)
            .setConverter(new GsonConverter(GSON))
            .setRequestInterceptor(new AuthRequestInterceptor())
            .build();

    final RetrofitAuthService API = REST_ADAPTER.create(RetrofitAuthService.class);

    @Override
    public void signIn(String email, String password, final BaseCallback<Token> cb) {
        API.getAuthToken(email, password, new Callback<Token>() {
            @Override
            public void success(Token token, Response response) {
                cb.onSuccess(token);
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
    public void get(int id, BaseCallback cb) {

    }

    @Override
    public void create(Object o, BaseCallback cb) {

    }

    @Override
    public void update(Object o, BaseCallback cb) {

    }

    @Override
    public void delete(int id, BaseCallback cb) {

    }

    @Override
    public void getAll(BaseCallback cb) {

    }
}
