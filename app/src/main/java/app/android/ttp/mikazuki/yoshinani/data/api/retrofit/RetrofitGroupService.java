package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface RetrofitGroupService {

    static final String PATH_GROUPS = "/groups";
    static final String PATH_GROUP_OVERVIEW_ID = "/groups/{id}";

    @GET(PATH_GROUP_OVERVIEW_ID)
    public void getOverView(@Path("id") int group_id, Callback<Group> cb);

    @GET(PATH_GROUPS)
    public void getAll(@Query("user_id") int user_id, Callback<List<Group>> cb);
}
