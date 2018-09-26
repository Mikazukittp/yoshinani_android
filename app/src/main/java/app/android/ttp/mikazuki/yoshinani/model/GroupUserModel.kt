package app.android.ttp.mikazuki.yoshinani.model

import org.parceler.Parcel

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.GroupUser
import rx.Observable

/**
 * @author haijimakazuki
 */
@Parcel
class GroupUserModel {

    var id: Int = 0
    var groupId: Int = 0
    var userId: Int = 0
    var status: String

    constructor(id: Int, groupId: Int, userId: Int, status: String) {
        this.id = id
        this.groupId = groupId
        this.userId = userId
        this.status = status
    }

    constructor() {}

    fun createEntity(): GroupUser {
        return GroupUser(
                this.id,
                this.groupId,
                this.userId,
                this.status
        )
    }

    companion object {

        fun from(entity: GroupUser): GroupUserModel {
            return GroupUserModel(
                    entity.id,
                    entity.groupId,
                    entity.userId,
                    entity.status
            )
        }

        fun from(entities: List<GroupUser>): List<GroupUserModel> {
            return Observable.from(entities).map { gu -> GroupUserModel.from(gu) }.toList().toBlocking().single()
        }
    }
}
