package app.android.ttp.mikazuki.yoshinani.repository.retrofit

import android.content.Context
import android.util.Log

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter

import java.io.IOException

import app.android.ttp.mikazuki.yoshinani.BuildConfig
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.APIError
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.interceptor.AuthRequestInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Converter
import retrofit2.GsonConverterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.RxJavaCallAdapterFactory

/**
 * @author haijimakazuki
 */
object ApiUtil {
    val API_URL = if (BuildConfig.SERVER_PRODUCTION) "http://52.197.232.13/api/v1/" else "http://52.193.62.129/api/v1/"

    /**
     * @return
     */
    val gsonConverterFactory: Converter.Factory
        get() {
            val GSON = GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create()
            return GsonConverterFactory.create(GSON)
        }

    /**
     * @param context
     * @param containsAuthInfo
     * @return
     */
    @JvmOverloads
    fun buildRESTAdapter(context: Context, containsAuthInfo: Boolean = true): Retrofit {
        val okHttpClientBuilder = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
        if (containsAuthInfo) {
            okHttpClientBuilder.addInterceptor(AuthRequestInterceptor(context))
        }
        return Retrofit.Builder()
                .baseUrl(ApiUtil.API_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(ApiUtil.gsonConverterFactory)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
    }

    /**
     * @param response
     * @return
     */
    fun getApiError(response: Response<*>): APIError {
        try {
            if (response.errorBody() != null) {
                val adapter = Gson().getAdapter<APIError>(APIError::class.java!!)
                val body = response.errorBody().string()
                return adapter.fromJson(body)
            }
            return APIError("", null)
        } catch (e: IOException) {
            Log.e(ApiUtil::class.java!!.getName(), e.message)
            return APIError("", null)
        }

    }
}
/**
 * @param context
 * @return
 */
