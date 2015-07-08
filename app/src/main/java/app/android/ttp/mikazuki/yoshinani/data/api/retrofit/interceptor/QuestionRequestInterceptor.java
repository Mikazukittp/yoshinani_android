package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor;

import android.content.Context;
import android.content.SharedPreferences;
import retrofit.RequestInterceptor;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class QuestionRequestInterceptor implements RequestInterceptor  {

    //このcontentに値をいれたいのですが、、、
    Context context;

    public QuestionRequestInterceptor() {
    }

    @Override
    public void intercept(RequestInterceptor.RequestFacade request) {
        // do something

        SharedPreferences sp = context.getSharedPreferences("LocalData", Context.MODE_PRIVATE);
        String token = sp.getString("token", "");
        String accessToken = "Bearer " + token;
        System.out.println(accessToken);
        request.addHeader("Authorization", accessToken);
    }

}
