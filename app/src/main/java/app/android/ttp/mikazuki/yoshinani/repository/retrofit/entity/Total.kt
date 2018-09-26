package app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity

/**
 * Created by haijimakazuki on 15/08/16.
 */
class Total {

    var paid: Double = 0.toDouble()
    var toPay: Double = 0.toDouble()
    var groupId: Int = 0
    var userId: Int = 0

    constructor() {}

    constructor(paid: Long, toPay: Long, groupId: Int, userId: Int) {
        this.paid = paid.toDouble()
        this.toPay = toPay.toDouble()
        this.groupId = groupId
        this.userId = userId
    }
}
