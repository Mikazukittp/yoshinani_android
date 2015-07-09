package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import retrofit.Callback;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitGroupService {

    static final String PATH_GROUPS = "/auth/groups";
    static final String PATH_GROUP_OVERVIEW_ID = "/api/groups/overview/{id}";

    @GET(PATH_GROUP_OVERVIEW_ID)
    public void getOverView(@Path("id") String group_id, Callback<List<User>> cb);

}
