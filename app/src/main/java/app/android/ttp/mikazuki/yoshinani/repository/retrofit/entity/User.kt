package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity

/**
 * @author haijimakazuki
 */
class User {

    var id: Int = 0
    var account: String = ""
    var username: String = ""
    var email: String = ""
    var token: String = ""
    var totals: List<Total> = arrayListOf()
    var activeGroups: List<Group> = arrayListOf()
    var invitedGroups: List<Group> = arrayListOf()

    constructor() {}

    constructor(id: Int, account: String, username: String, email: String, token: String) {
        this.id = id
        this.account = account
        this.username = username
        this.email = email
        this.token = token
    }
}
