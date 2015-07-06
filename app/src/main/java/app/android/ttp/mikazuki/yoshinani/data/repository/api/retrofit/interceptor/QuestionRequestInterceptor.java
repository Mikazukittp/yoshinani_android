package app.android.ttp.mikazuki.yoshinani.data.repository.api.retrofit.interceptor;

import retrofit.RequestInterceptor;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class QuestionRequestInterceptor implements RequestInterceptor {

    public QuestionRequestInterceptor() {
    }

    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        // do something
        // request.addHeader("uid", "1");
    }

}
