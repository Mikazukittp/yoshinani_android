package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import retrofit.Callback;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitUserService {

    static final String PATH_USERS = "/users";
    static final String PATH_USER_WITH_ID = "/users/{id}";

    // 未実装(引数などは適当なので変えてください)
    @FormUrlEncoded
    @GET(PATH_USER_WITH_ID)
    public void getUserById(@Path("id") String user_id, Callback<User> cb);

}
