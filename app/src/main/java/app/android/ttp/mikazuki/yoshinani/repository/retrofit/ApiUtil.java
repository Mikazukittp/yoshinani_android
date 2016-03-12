package app.android.ttp.mikazuki.yoshinani.repository.retrofit;

import android.content.Context;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;

import java.io.IOException;

import app.android.ttp.mikazuki.yoshinani.BuildConfig;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.APIError;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.interceptor.AuthRequestInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Converter;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

/**
 * @author haijimakazuki
 */
public class ApiUtil {
    public static final String API_URL = BuildConfig.SERVER_PRODUCTION ? "http://52.69.32.124/api/v1/" : "http://52.193.62.129/api/v1/";

    /**
     * @return
     */
    public static Converter.Factory getGsonConverterFactory() {
        Gson GSON = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return GsonConverterFactory.create(GSON);
    }

    /**
     * @param context
     * @return
     */
    public static Retrofit buildRESTAdapter(Context context) {
        return buildRESTAdapter(context, true);
    }

    /**
     * @param context
     * @param containsAuthInfo
     * @return
     */
    public static Retrofit buildRESTAdapter(Context context, boolean containsAuthInfo) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BODY));
        if (containsAuthInfo) {
            okHttpClientBuilder.addInterceptor(new AuthRequestInterceptor(context));
        }
        return new Retrofit.Builder()
                .baseUrl(ApiUtil.API_URL)
                .client(okHttpClientBuilder.build())
                .addConverterFactory(ApiUtil.getGsonConverterFactory())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * @param response
     * @return
     */
    public static APIError getApiError(Response response) {
        try {
            if (response.errorBody() != null) {
                TypeAdapter<APIError> adapter = new Gson().getAdapter(APIError.class);
                String body = response.errorBody().string();
                return adapter.fromJson(body);
            }
            return new APIError("", null);
        } catch (IOException e) {
            Log.e(ApiUtil.class.getName(), e.getMessage());
            return new APIError("", null);
        }
    }
}
