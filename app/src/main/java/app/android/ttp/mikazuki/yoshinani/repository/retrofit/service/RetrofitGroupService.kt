package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service

import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Group
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.GroupUser
import retrofit2.Response
import retrofit2.http.*
import rx.Observable
import java.util.*

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
        internal var group: PostData

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
        internal var groupUser: MutableList<PostData>

        init {
            this.groupUser = ArrayList()
            this.groupUser.add(PostData(userId))
        }

        internal inner class PostData(var userId: Int)
    }

    companion object {

        const val PATH_GROUPS = "groups"
        const val PATH_GROUP_WITH_ID = "groups/{id}"
        const val PATH_GROUP_USER = "groups/{group_id}/users"
        const val PATH_GROUP_USER_ACCEPT = "groups/{group_id}/users/accept"
    }
}
