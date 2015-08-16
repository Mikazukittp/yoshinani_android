package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor;

import android.content.Context;
import android.content.SharedPreferences;

import retrofit.RequestInterceptor;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class BaseRequestInterceptor implements RequestInterceptor {

    Context mContext;

    public BaseRequestInterceptor() {
    }

    public BaseRequestInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public void intercept(RequestFacade request) {

        SharedPreferences sp = mContext.getSharedPreferences("LocalData", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        String accessToken = "Bearer " + token;
        request.addHeader("Authorization", accessToken);
    }

}
