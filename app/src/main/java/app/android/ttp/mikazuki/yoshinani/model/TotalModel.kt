package app.android.ttp.mikazuki.yoshinani.model

import org.parceler.Parcel

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.Total
import rx.Observable

/**
 * @author haijimakazuki
 */
@Parcel
class TotalModel {

    var paid: Double = 0.toDouble()
    var toPay: Double = 0.toDouble()
    var groupId: Int = 0
    var userId: Int = 0

    val result: Int
        get() = Math.round(paid - toPay).toInt()

    constructor() {}

    constructor(paid: Double, toPay: Double, groupId: Int, userId: Int) {
        this.paid = paid
        this.toPay = toPay
        this.groupId = groupId
        this.userId = userId
    }

    companion object {

        fun from(entity: Total): TotalModel {
            return TotalModel(
                    entity.paid,
                    entity.toPay,
                    entity.groupId,
                    entity.userId
            )
        }

        fun from(entities: List<Total>): List<TotalModel> {
            return Observable
                    .from(entities)
                    .map { t -> TotalModel.from(t) }
                    .toList()
                    .toBlocking()
                    .single()
        }
    }
}
