package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitUserService {

    static final String PATH_USERS = "/api/users";
    static final String PATH_USERS_ME = "/api/users/me";

    @GET(PATH_USERS)
    public void getUsers(Callback<List<User>> cb);

    @GET(PATH_USERS_ME)
    public void getMe(Callback<User> cb);

}
