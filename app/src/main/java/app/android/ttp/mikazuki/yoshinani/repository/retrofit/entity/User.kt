package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity

/**
 * @author haijimakazuki
 */
class User {

    var id: Int = 0
    var account: String? = null
    var username: String? = null
    var email: String? = null
    var token: String? = null
    var totals: List<Total>? = null
    var activeGroups: List<Group>? = null
    var invitedGroups: List<Group>? = null

    constructor() {}

    constructor(id: Int, account: String, username: String, email: String, token: String) {
        this.id = id
        this.account = account
        this.username = username
        this.email = email
        this.token = token
    }
}
