package app.android.ttp.mikazuki.yoshinani.repository.preference

import android.content.Context
import android.content.SharedPreferences

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User

/**
 * @author haijimakazuki
 */
object PreferenceUtil {

    private val UID_KEY = "uid"
    private val TOKEN_KEY = "token"
    private val ME_KEY = "me"
    private val NOTIFICATION_TOKEN_KEY = "notificationToken"
    private val PREFERENCE_NAME = "LocalData"

    fun putUserData(context: Context, user: User) {
        val editor = getPreference(context).edit()
        editor.putString(UID_KEY, user.id.toString() + "")
        editor.putString(TOKEN_KEY, user.token)
        editor.apply()
    }

    fun getUid(context: Context): String? {
        return getPreference(context).getString(UID_KEY, null)
    }

    fun getToken(context: Context): String? {
        return getPreference(context).getString(TOKEN_KEY, null)
    }

    fun clearUserData(context: Context) {
        getPreference(context).edit().clear().commit()
    }

    fun isUserDataStored(context: Context): Boolean {
        return getUid(context) != null && getToken(context) != null
    }

    fun putNotificationToken(context: Context, notificationToken: String) {
        getPreference(context).edit().putString(NOTIFICATION_TOKEN_KEY, notificationToken).apply()
    }

    fun getNotificationToken(context: Context): String? {
        return getPreference(context).getString(NOTIFICATION_TOKEN_KEY, null)
    }

    private fun getPreference(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

}
