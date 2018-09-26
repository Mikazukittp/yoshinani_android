package app.android.ttp.mikazuki.yoshinani.model


import org.parceler.Parcel

import java.util.ArrayList

import app.android.ttp.mikazuki.yoshinani.binding.BindableString
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Group
import rx.Observable

/**
 * @author haijimakazuki
 */
@Parcel
class GroupModel {

    var id: Int = 0
    var name = BindableString()
        internal set
    var description = BindableString()
        internal set
    var members: List<UserModel> = ArrayList()
    var invitedMembers: List<UserModel> = ArrayList()

    constructor() {}

    constructor(id: Int, name: String, description: String, members: List<UserModel>, invitedMembers: List<UserModel>) {
        this.id = id
        this.name.set(name)
        this.description.set(description)
        this.members = members
        this.invitedMembers = invitedMembers
    }

    fun createEntity(): Group {
        return Group(
                this.id,
                this.name.get(),
                this.description.get(),
                Observable.from(this.members).map<User>(Func1<UserModel, User> { it.createEntity() }).toList().toBlocking().single(),
                Observable.from(this.invitedMembers).map<User>(Func1<UserModel, User> { it.createEntity() }).toList().toBlocking().single()
        )
    }

    fun setName(name: String) {
        this.name.set(name)
    }

    fun setDescription(description: String) {
        this.description.set(description)
    }

    companion object {

        fun from(entity: Group?): GroupModel? {
            return if (entity == null) null else GroupModel(
                    entity.id,
                    entity.name,
                    entity.description,
                    UserModel.from(entity.activeUsers),
                    UserModel.from(entity.invitedUsers)
            )
        }

        fun from(entities: List<Group>?): List<GroupModel>? {
            return if (entities == null) null else Observable.from(entities).map<GroupModel>(Func1<Group, GroupModel> { from(it) }).toList().toBlocking().single()
        }
    }
}