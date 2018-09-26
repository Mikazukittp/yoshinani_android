package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service

import java.util.ArrayList

import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Group
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.GroupUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * @author haijimakazuki
 */
interface RetrofitGroupService {

    @GET(PATH_GROUPS)
    fun getAll(@Query("user_id") userId: Int): Observable<Response<List<Group>>>

    @GET(PATH_GROUP_WITH_ID)
    operator fun get(@Path("id") groupId: Int): Observable<Response<Group>>

    @POST(PATH_GROUPS)
    fun create(@Body data: RequestDataOnCreate): Observable<Response<Group>>

    @PATCH(PATH_GROUP_WITH_ID)
    fun update(@Path("id") groupId: Int, @Body data: RequestDataOnCreate): Observable<Response<Group>>

    @POST(PATH_GROUP_USER)
    fun invite(@Path("group_id") groupId: Int,
               @Body data: RequestDataOnInvite): Observable<Response<Group>>

    @PATCH(PATH_GROUP_USER_ACCEPT)
    fun accept(@Path("group_id") groupId: Int): Observable<Response<GroupUser>>

    class RequestDataOnCreate(group: GroupModel) {
        var group: PostData

        init {
            this.group = PostData(group.createEntity())
        }

        internal inner class PostData(group: Group) {
            var name: String? = null
            var description: String? = null

            init {
                this.name = group.name
                this.description = group.description
            }
        }
    }

    class RequestDataOnInvite(userId: Int) {
        var groupUser: MutableList<PostData>

        init {
            this.groupUser = ArrayList()
            this.groupUser.add(PostData(userId))
        }

        internal inner class PostData(var userId: Int)
    }

    companion object {

        val PATH_GROUPS = "groups"
        val PATH_GROUP_WITH_ID = "groups/{id}"
        val PATH_GROUP_USER = "groups/{group_id}/users"
        val PATH_GROUP_USER_ACCEPT = "groups/{group_id}/users/accept"
    }
}
