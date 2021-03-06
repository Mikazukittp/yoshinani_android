package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import javax.annotation.Nonnull;

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.ResponseMessage;
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    static final String PATH_PASSWORDS_INIT = "passwords/init";
    static final String PATH_PASSWORDS_RESET = "passwords/reset";
    static final String PATH_NOTIFICATION_TOKENS = "notification_tokens";
    static final String PATH_OAUTH = "oauth_registrations";

    @POST(PATH_USERS)
    public Observable<Response<User>> createUser(@Body RequestWrapper user);

    @GET(PATH_USERS)
    public Observable<Response<List<User>>> getUsers(@Query("group_id") int groupId);

    @GET(PATH_USER)
    public Observable<Response<User>> getMe(@Path("id") String userId);

    @PATCH(PATH_USER)
    public Observable<Response<User>> updateUser(@Path("id") int userId, @Body UpdateRequestWrapper user);

    @PATCH(PATH_USER)
    public Observable<Response<User>> registerAccount(@Path("id") int userId, @Body UpdateAccountRequestWrapper user);

    @FormUrlEncoded
    @POST(PATH_LOGIN)
    public Observable<Response<User>> getAuthToken(@Field("account") String account, @Field("password") String password);

    @GET(PATH_USER_SEARCH)
    public Observable<Response<User>> search(@Query("account") String account);

    @PATCH(PATH_PASSWORDS)
    public Observable<Response<User>> changePassword(@Body ChangePasswordRequestWrapper user);

    @POST(PATH_PASSWORDS_INIT)
    public Observable<Response<ResponseMessage>> forgetPassword(@Body ForgetRequestWrapper user);

    @PATCH(PATH_PASSWORDS_RESET)
    public Observable<Response<User>> resetPassword(@Body ResetPasswordRequestWrapper user);

    @POST(PATH_NOTIFICATION_TOKENS)
    public Observable<Response<User>> registerToken(@Body NotificationRequestWrapper notification);

    @PATCH(PATH_NOTIFICATION_TOKENS)
    public Observable<Response<User>> updateToken(@Body NotificationRequestWrapper notification);

    @DELETE(PATH_NOTIFICATION_TOKENS)
    public Observable<Response<User>> deleteToken(@Body NotificationRequestWrapper notification);

    @POST(PATH_OAUTH)
    public Observable<Response<User>> signInWithOAuth(@Body OAuthRequestWrapper oauthRegistration);

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

    public class UpdateRequestWrapper {
        public PatchData user;

        public UpdateRequestWrapper(@NonNull final String username,
                                    @NonNull final String email) {
            this.user = new PatchData(username, email);
        }

        class PatchData {
            public String username;
            public String email;

            public PatchData(@NonNull final String username,
                             @NonNull final String email) {
                this.username = username;
                this.email = email;
            }
        }
    }

    public class UpdateAccountRequestWrapper {
        public PatchData user;

        public UpdateAccountRequestWrapper(@NonNull final String account) {
            this.user = new PatchData(account);
        }

        class PatchData {
            public String account;

            public PatchData(@NonNull final String account) {
                this.account = account;
            }
        }
    }

    public class ChangePasswordRequestWrapper {
        public PatchData user;

        public ChangePasswordRequestWrapper(@NonNull final String password,
                                            @NonNull final String newPassword,
                                            @NonNull final String newPasswordConfirmation) {
            this.user = new PatchData(password, newPassword, newPasswordConfirmation);
        }

        class PatchData {
            public String password;
            public String newPassword;
            public String newPasswordConfirmation;

            public PatchData(@NonNull final String password,
                             @NonNull final String newPassword,
                             @NonNull final String newPasswordConfirmation) {
                this.password = password;
                this.newPassword = newPassword;
                this.newPasswordConfirmation = newPasswordConfirmation;
            }
        }
    }

    public class ForgetRequestWrapper {
        public PostData user;

        public ForgetRequestWrapper(@NonNull final CharSequence email) {
            this.user = new PostData(email);
        }

        class PostData {
            public CharSequence email;

            public PostData(@NonNull final CharSequence email) {
                this.email = email;
            }
        }
    }

    public class ResetPasswordRequestWrapper {
        public PatchData user;

        public ResetPasswordRequestWrapper(@NonNull final CharSequence resetPasswordToken,
                                           @NonNull final CharSequence newPassword,
                                           @NonNull final CharSequence newPasswordConfirmation) {
            this.user = new PatchData(resetPasswordToken, newPassword, newPasswordConfirmation);
        }

        class PatchData {
            public CharSequence resetPasswordToken;
            public CharSequence newPassword;
            public CharSequence newPasswordConfirmation;

            public PatchData(@NonNull final CharSequence resetPasswordToken,
                             @NonNull final CharSequence newPassword,
                             @NonNull final CharSequence newPasswordConfirmation) {
                this.resetPasswordToken = resetPasswordToken;
                this.newPassword = newPassword;
                this.newPasswordConfirmation = newPasswordConfirmation;
            }
        }
    }

    public class NotificationRequestWrapper {
        public Data notificationToken;

        public NotificationRequestWrapper(@NonNull final String deviceToken,
                                          @Nullable final String authDeviceToken) {
            this.notificationToken = new Data(deviceToken, authDeviceToken);
        }

        class Data {
            public final String deviceType = "android";
            public String deviceToken;
            public String authDeviceToken;

            public Data(@NonNull final String deviceToken,
                        @Nullable final String authDeviceToken) {
                this.deviceToken = deviceToken;
                this.authDeviceToken = authDeviceToken;
            }
        }
    }

    public class OAuthRequestWrapper {
        public Data oauthRegistration;

        public OAuthRequestWrapper(@Nonnull final String thirdPartyId,
                                   @Nonnull final int oauthId,
                                   @Nonnull final String snsHashId) {
            this.oauthRegistration = new Data(thirdPartyId, oauthId, snsHashId);
        }

        class Data {
            public String thirdPartyId;
            public int oauthId;
            public String snsHashId;

            public Data(@Nonnull final String thirdPartyId,
                        @Nonnull final int oauthId,
                        @Nonnull final String snsHashId) {
                this.thirdPartyId = thirdPartyId;
                this.oauthId = oauthId;
                this.snsHashId = snsHashId;
            }
        }

    }
}
