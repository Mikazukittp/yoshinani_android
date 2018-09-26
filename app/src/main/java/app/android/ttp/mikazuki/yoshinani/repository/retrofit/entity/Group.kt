package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity

/**
 * Created by haijimakazuki on 15/07/09.
 */
class Group {

    var id: Int = 0
    var name: String? = null
    var description: String? = null
    var activeUsers: List<User>? = null
    var invitedUsers: List<User>? = null

    constructor() {}

    constructor(id: Int, name: String, description: String, activeUsers: List<User>, invitedUsers: List<User>) {
        this.id = id
        this.name = name
        this.description = description
        this.activeUsers = activeUsers
        this.invitedUsers = invitedUsers
    }
}
