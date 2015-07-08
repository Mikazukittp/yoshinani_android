package app.android.ttp.mikazuki.yoshinani.domain.entity;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by matsuMac on 2015/07/08.
 */
public class Token {
    private SharedPreferences sharedPreferences;
    private static String PREF_NAME = "LocalData";

    private String token;

    public Token() {
    }

    public Token(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static String getTokenFromPrefs(Context context) {
        return getPrefs(context).getString("token", "default_token");
    }

    public static void setTokenFromPrefs(Context context, String input) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("token", input);
        editor.apply();
    }


    public void setToken(String token) {
        this.token = token;
    }
}
