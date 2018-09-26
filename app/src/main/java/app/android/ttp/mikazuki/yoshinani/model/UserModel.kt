package app.android.ttp.mikazuki.yoshinani.model

import android.content.Context
import android.graphics.drawable.Drawable

import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator

import org.parceler.Parcel

import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User
import rx.Observable

/**
 * @author haijimakazuki
 */
@Parcel
class UserModel {

    var id: Int = 0
    var account: String
    var username: String? = null
    var email: String
    var token: String
    var totals: List<TotalModel>
    var groups: List<GroupModel>
    var invitedGroups: List<GroupModel>

    val icon: Drawable
        get() = TextDrawable.builder()
                .beginConfig()
                .bold()
                .toUpperCase()
                .endConfig()
                .buildRound(
                        account[0].toString(),
                        ColorGenerator.MATERIAL.getColor(account)
                )

    val displayName: String?
        get() = if (username != null) username else account

    constructor() {}

    constructor(id: Int, account: String, username: String, email: String, token: String, totals: List<TotalModel>, groups: List<GroupModel>, invitedGroups: List<GroupModel>) {
        this.id = id
        this.account = account
        this.username = username
        this.email = email
        this.token = token
        this.totals = totals
        this.groups = groups
        this.invitedGroups = invitedGroups
    }

    fun createEntity(): User {
        return User(
                this.id,
                this.account,
                this.username,
                this.email,
                this.token
        )
    }

    fun isMe(context: Context): Boolean {
        return id == Integer.parseInt(PreferenceUtil.getUid(context)!!)
    }

    companion object {

        fun from(entity: User?): UserModel? {
            return if (entity == null) null else UserModel(
                    entity.id,
                    entity.account,
                    entity.username,
                    entity.email,
                    entity.token,
                    TotalModel.from(entity.totals),
                    GroupModel.from(entity.activeGroups),
                    GroupModel.from(entity.invitedGroups)
            )
        }

        fun from(entities: List<User>?): List<UserModel>? {
            return if (entities == null) null else Observable
                    .from(entities)
                    .map<UserModel> { u -> UserModel.from(u) }
                    .toList()
                    .toBlocking()
                    .single()
        }
    }
}
