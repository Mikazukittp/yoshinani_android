package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity

/**
 * Created by haijimakazuki on 15/07/09.
 */
class Payment {

    var id: Int = 0
    var amount: Int = 0
    var event: String? = null
    var description: String? = null
    var date: String? = null
    var paidUser: User? = null
    var participants: List<User>? = null
    var groupId: Int = 0
    var isRepayment: Boolean = false

    constructor() {}

    constructor(id: Int, amount: Int, event: String, description: String, date: String, paidUser: User, participants: List<User>, groupId: Int, isRepayment: Boolean) {
        this.id = id
        this.amount = amount
        this.event = event
        this.description = description
        this.date = date
        this.paidUser = paidUser
        this.participants = participants
        this.groupId = groupId
        this.isRepayment = isRepayment
    }
}
