package app.android.ttp.mikazuki.yoshinani.repository.preference;

import android.content.Context;
import android.content.SharedPreferences;

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;

/**
 * @author haijimakazuki
 */
public class PreferenceUtil {

    private static final String UID_KEY = "uid";
    private static final String TOKEN_KEY = "token";
    private static final String ME_KEY = "me";
    private static final String NOTIFICATION_TOKEN_KEY = "notificationToken";
    private static final String PREFERENCE_NAME = "LocalData";

    public static void putUserData(Context context, User user) {
        SharedPreferences.Editor editor = getPreference(context).edit();
        editor.putString(UID_KEY, user.getId() + "");
        editor.putString(TOKEN_KEY, user.getToken());
        editor.apply();
    }

    public static String getUid(Context context) {
        return getPreference(context).getString(UID_KEY, null);
    }

    public static String getToken(Context context) {
        return getPreference(context).getString(TOKEN_KEY, null);
    }

    public static void clearUserData(Context context) {
        getPreference(context).edit().clear().commit();
    }

    public static boolean isUserDataStored(Context context) {
        return getUid(context) != null && getToken(context) != null;
    }

    public static void putNotificationToken(Context context, String notificationToken) {
        getPreference(context).edit().putString(NOTIFICATION_TOKEN_KEY, notificationToken).apply();
    }

    public static String getNotificationToken(Context context) {
        return getPreference(context).getString(NOTIFICATION_TOKEN_KEY, null);
    }

    private static SharedPreferences getPreference(Context context) {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

}
