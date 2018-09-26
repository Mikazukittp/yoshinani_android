package app.android.ttp.mikazuki.yoshinani.repository.retrofit.interceptor

import android.content.Context

import org.greenrobot.eventbus.EventBus

import java.io.IOException

import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * Created by haijimakazuki on 15/07/07.
 */
class AuthRequestInterceptor : Interceptor {

    internal var mContext: Context

    constructor() {}

    constructor(context: Context) {
        this.mContext = context
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
                .newBuilder()
                .addHeader("UID", PreferenceUtil.getUid(mContext)!!)
                .addHeader("TOKEN", PreferenceUtil.getToken(mContext)!!)
                .build()

        val response = chain.proceed(request)

        if (response.code() == 401) {
            PreferenceUtil.clearUserData(mContext)
            EventBus.getDefault().post(UnauthorizedEvent())
        }
        return response
    }
}
