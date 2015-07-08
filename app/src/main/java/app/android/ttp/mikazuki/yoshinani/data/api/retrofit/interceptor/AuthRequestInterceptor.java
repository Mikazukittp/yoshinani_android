package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor;

import retrofit.RequestInterceptor;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class AuthRequestInterceptor implements RequestInterceptor {

    public AuthRequestInterceptor() {
    }

    @Override
    public void intercept(RequestFacade request) {
        // do something
        // request.addHeader("uid", "1");
    }

}
