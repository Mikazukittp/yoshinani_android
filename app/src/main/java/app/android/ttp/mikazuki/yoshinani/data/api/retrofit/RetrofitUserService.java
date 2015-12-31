package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitUserService {

    static final String PATH_USERS = "/users";
    static final String PATH_LOGIN = "/users/sign_in";

    @GET(PATH_USERS)
    public void getUsers(@Query("group_id")int groupId, Callback<List<User>> cb);

    @FormUrlEncoded
    @POST(PATH_LOGIN)
    public void getAuthToken(@Field("account") String account, @Field("password") String password, Callback<User> cb);

}
