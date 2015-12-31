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
        int uid = sp.getInt("uid", -1);
        String token = sp.getString("token", "");
        request.addHeader("UID", uid+"");
        request.addHeader("TOKEN", token);
    }

}
