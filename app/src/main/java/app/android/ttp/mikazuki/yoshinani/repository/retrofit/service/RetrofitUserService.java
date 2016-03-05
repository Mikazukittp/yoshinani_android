package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service;

import android.support.annotation.NonNull;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @author haijimakazuki
 */
public interface RetrofitUserService {

    static final String PATH_USERS = "users";
    static final String PATH_USER = "users/{id}";
    static final String PATH_LOGIN = "users/sign_in";
    static final String PATH_USER_SEARCH = "users/search";
    static final String PATH_PASSWORDS = "passwords";

    @POST(PATH_USERS)
    public Observable<Response<User>> createUser(@Body RequestWrapper user);

    @GET(PATH_USERS)
    public Observable<Response<List<User>>> getUsers(@Query("group_id") int groupId);

    @GET(PATH_USER)
    public Observable<Response<User>> getMe(@Path("id") String userId);

    @FormUrlEncoded
    @POST(PATH_LOGIN)
    public Observable<Response<User>> getAuthToken(@Field("account") String account, @Field("password") String password);

    @GET(PATH_USER_SEARCH)
    public Observable<Response<User>> search(@Query("account") String account);

    @PATCH(PATH_PASSWORDS)
    public Observable<Response<User>> changePassword(@Field("password") String oldPassword,
                                                     @Field("new_password") String newPassword,
                                                     @Field("new_password_confirmation") String newPasswordConfirm);

    public class RequestWrapper {
        public PostData user;

        public RequestWrapper(@NonNull final String account,
                              @NonNull final String email,
                              @NonNull final String password) {
            this.user = new PostData(account, email, password);
        }

        class PostData {
            public String account;
            public String email;
            public String password;

            public PostData(@NonNull final String account,
                            @NonNull final String email,
                            @NonNull final String password) {
                this.account = account;
                this.email = email;
                this.password = password;
            }
        }
    }
}
