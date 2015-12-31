package app.android.ttp.mikazuki.yoshinani.data.api;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor.BaseRequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by haijimakazuki on 15/07/06.
 */
public class ApiUtil {
    public static final String API_URL = "http://52.69.32.124/api/v1";
//    public static final String GROUP_ID = "559eaa98d430387816a640b1";
    private static final String TAG = "retrofit API connection";

    public static GsonConverter getGsonConverter() {
        Gson GSON = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return new GsonConverter(GSON);
    }

    public static RestAdapter buildRESTAdapter(Context context){
        return new RestAdapter.Builder()
                .setEndpoint(ApiUtil.API_URL)
                .setConverter(ApiUtil.getGsonConverter())
                .setRequestInterceptor(new BaseRequestInterceptor(context))
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setLog(new RestAdapter.Log() {
                    @Override
                    public void log(String msg) {
                        Log.i(TAG, msg);
                    }
                })
                .build();
    }
}
