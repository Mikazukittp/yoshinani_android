package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Token;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public interface RetrofitAuthService {

    static final String PATH_QUESTION = "/auth/local";

    @FormUrlEncoded
    @POST(PATH_QUESTION)
    public void getAuthToken(@Field("email") String email, @Field("password") String password, Callback<Token> cb);

}
