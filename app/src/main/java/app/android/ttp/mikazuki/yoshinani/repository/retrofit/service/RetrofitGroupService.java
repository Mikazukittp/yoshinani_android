package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Group;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.GroupUser;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author haijimakazuki
 */
public interface RetrofitGroupService {

    static final String PATH_GROUPS = "groups";
    static final String PATH_GROUP_WITH_ID = "groups/{id}";
    static final String PATH_GROUP_USER = "groups/{group_id}/users";
    static final String PATH_GROUP_USER_ACCEPT = "groups/{group_id}/users/accept";

    @GET(PATH_GROUPS)
    public Observable<Response<List<Group>>> getAll(@Query("user_id") int userId);

    @GET(PATH_GROUP_WITH_ID)
    public Observable<Response<Group>> get(@Path("id") int groupId);

    @POST(PATH_GROUPS)
    public Observable<Response<Group>> create(@Body RequestDataOnCreate data);

    @PATCH(PATH_GROUP_WITH_ID)
    public Observable<Response<Group>> update(@Path("id") int groupId, @Body RequestDataOnCreate data);

    @POST(PATH_GROUP_USER)
    public Observable<Response<Group>> invite(@Path("group_id") int groupId,
                                              @Body RequestDataOnInvite data);

    @PATCH(PATH_GROUP_USER_ACCEPT)
    public Observable<Response<GroupUser>> accept(@Path("group_id") int groupId);

    public class RequestDataOnCreate {
        public PostData group;

        public RequestDataOnCreate(@NonNull final GroupModel group) {
            this.group = new PostData(group.createEntity());
        }

        class PostData {
            public String name;
            public String description;

            public PostData(@NonNull final Group group) {
                this.name = group.getName();
                this.description = group.getDescription();
            }
        }
    }

    public class RequestDataOnInvite {
        public List<PostData> groupUser;

        public RequestDataOnInvite(@NonNull final int userId) {
            this.groupUser = Lists.newArrayList(new PostData(userId));
        }

        class PostData {
            public int userId;

            public PostData(@NonNull final int userId) {
                this.userId = userId;
            }
        }
    }
}
