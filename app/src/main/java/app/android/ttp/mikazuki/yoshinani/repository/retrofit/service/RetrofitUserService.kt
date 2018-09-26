package app.android.ttp.mikazuki.yoshinani.repository.retrofit.service

import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.ResponseMessage
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.entity.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import rx.Observable

/**
 * @author haijimakazuki
 */
interface RetrofitUserService {

    @POST(PATH_USERS)
    fun createUser(@Body user: RequestWrapper): Observable<Response<User>>

    @GET(PATH_USERS)
    fun getUsers(@Query("group_id") groupId: Int): Observable<Response<List<User>>>

    @GET(PATH_USER)
    fun getMe(@Path("id") userId: String): Observable<Response<User>>

    @PATCH(PATH_USER)
    fun updateUser(@Path("id") userId: Int, @Body user: UpdateRequestWrapper): Observable<Response<User>>

    @PATCH(PATH_USER)
    fun registerAccount(@Path("id") userId: Int, @Body user: UpdateAccountRequestWrapper): Observable<Response<User>>

    @FormUrlEncoded
    @POST(PATH_LOGIN)
    fun getAuthToken(@Field("account") account: String, @Field("password") password: String): Observable<Response<User>>

    @GET(PATH_USER_SEARCH)
    fun search(@Query("account") account: String): Observable<Response<User>>

    @PATCH(PATH_PASSWORDS)
    fun changePassword(@Body user: ChangePasswordRequestWrapper): Observable<Response<User>>

    @POST(PATH_PASSWORDS_INIT)
    fun forgetPassword(@Body user: ForgetRequestWrapper): Observable<Response<ResponseMessage>>

    @PATCH(PATH_PASSWORDS_RESET)
    fun resetPassword(@Body user: ResetPasswordRequestWrapper): Observable<Response<User>>

    @POST(PATH_NOTIFICATION_TOKENS)
    fun registerToken(@Body notification: NotificationRequestWrapper): Observable<Response<User>>

    @PATCH(PATH_NOTIFICATION_TOKENS)
    fun updateToken(@Body notification: NotificationRequestWrapper): Observable<Response<User>>

    @DELETE(PATH_NOTIFICATION_TOKENS)
    fun deleteToken(@Body notification: NotificationRequestWrapper): Observable<Response<User>>

    @POST(PATH_OAUTH)
    fun signInWithOAuth(@Body oauthRegistration: OAuthRequestWrapper): Observable<Response<User>>

    class RequestWrapper(account: String,
                         email: String,
                         password: String) {
        var user: PostData

        init {
            this.user = PostData(account, email, password)
        }

        internal inner class PostData(var account: String,
                                      var email: String,
                                      var password: String)
    }

    class UpdateRequestWrapper(username: String,
                               email: String) {
        var user: PatchData

        init {
            this.user = PatchData(username, email)
        }

        internal inner class PatchData(var username: String,
                                       var email: String)
    }

    class UpdateAccountRequestWrapper(account: String) {
        var user: PatchData

        init {
            this.user = PatchData(account)
        }

        internal inner class PatchData(var account: String)
    }

    class ChangePasswordRequestWrapper(password: String,
                                       newPassword: String,
                                       newPasswordConfirmation: String) {
        var user: PatchData

        init {
            this.user = PatchData(password, newPassword, newPasswordConfirmation)
        }

        internal inner class PatchData(var password: String,
                                       var newPassword: String,
                                       var newPasswordConfirmation: String)
    }

    class ForgetRequestWrapper(email: CharSequence) {
        var user: PostData

        init {
            this.user = PostData(email)
        }

        internal inner class PostData(var email: CharSequence)
    }

    class ResetPasswordRequestWrapper(resetPasswordToken: CharSequence,
                                      newPassword: CharSequence,
                                      newPasswordConfirmation: CharSequence) {
        var user: PatchData

        init {
            this.user = PatchData(resetPasswordToken, newPassword, newPasswordConfirmation)
        }

        internal inner class PatchData(var resetPasswordToken: CharSequence,
                                       var newPassword: CharSequence,
                                       var newPasswordConfirmation: CharSequence)
    }

    class NotificationRequestWrapper(deviceToken: String,
                                     authDeviceToken: String?) {
        var notificationToken: Data

        init {
            this.notificationToken = Data(deviceToken, authDeviceToken)
        }

        internal inner class Data(var deviceToken: String,
                                  var authDeviceToken: String?) {
            val deviceType = "android"
        }
    }

    class OAuthRequestWrapper(thirdPartyId: String,
                              oauthId: Int,
                              snsHashId: String) {
        var oauthRegistration: Data

        init {
            this.oauthRegistration = Data(thirdPartyId, oauthId, snsHashId)
        }

        internal inner class Data(var thirdPartyId: String,
                                  var oauthId: Int,
                                  var snsHashId: String)

    }

    companion object {

        val PATH_USERS = "users"
        val PATH_USER = "users/{id}"
        val PATH_LOGIN = "users/sign_in"
        val PATH_USER_SEARCH = "users/search"
        val PATH_PASSWORDS = "passwords"
        val PATH_PASSWORDS_INIT = "passwords/init"
        val PATH_PASSWORDS_RESET = "passwords/reset"
        val PATH_NOTIFICATION_TOKENS = "notification_tokens"
        val PATH_OAUTH = "oauth_registrations"
    }
}