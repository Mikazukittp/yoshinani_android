package app.android.ttp.mikazuki.yoshinani.repository.retrofit.interceptor;

import android.content.Context;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class AuthRequestInterceptor implements Interceptor {

    Context mContext;

    public AuthRequestInterceptor() {
    }

    public AuthRequestInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("UID", PreferenceUtil.getUid(mContext))
                .addHeader("TOKEN", PreferenceUtil.getToken(mContext))
                .build();

        Response response = chain.proceed(request);

        if (response.code() == 401) {
            PreferenceUtil.clearUserData(mContext);
            EventBus.getDefault().post(new UnauthorizedEvent());
        }
        return response;
    }
}
