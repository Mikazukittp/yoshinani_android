package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import app.android.ttp.mikazuki.yoshinani.BuildConfig
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.ErrorEvent
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent
import app.android.ttp.mikazuki.yoshinani.event.ShowSnackbarEvent
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.repository.retrofit.ApiUtil
import app.android.ttp.mikazuki.yoshinani.services.AuthService
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils
import app.android.ttp.mikazuki.yoshinani.view.activity.MainActivity
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.jakewharton.rxbinding.widget.RxTextView
import jp.line.android.sdk.LineSdkContextManager
import jp.line.android.sdk.exception.LineSdkLoginError
import jp.line.android.sdk.exception.LineSdkLoginException
import jp.line.android.sdk.login.LineLoginFuture
import org.greenrobot.eventbus.EventBus
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.math.BigInteger
import java.security.MessageDigest

/**
 * @author haijimakazuki
 */
class SignInFragment : Fragment() {

    @BindView(R.id.account)
    internal var mAccount: EditText? = null
    @BindView(R.id.password)
    internal var mPassword: EditText? = null
    @BindView(R.id.login_btn)
    internal var mLogin: Button? = null
    private var mUnbinder: Unbinder? = null

    private var mAuthService: AuthService? = null
    private var mUserService: UserService? = null

    private val compositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)
        mUnbinder = ButterKnife.bind(this, view)
        mAuthService = AuthService(activity!!.applicationContext)
        mUserService = UserService(activity!!.applicationContext)

        // バリデーション
        val isAccountCompleted = RxTextView.textChanges(mAccount!!).map { str -> !TextUtils.isEmpty(str) }
        val isPasswordCompleted = RxTextView.textChanges(mPassword!!).map { str -> !TextUtils.isEmpty(str) }
        val isValidAll = Observable.combineLatest(isAccountCompleted, isPasswordCompleted) { a, p -> a!! && p!! }
        compositeSubscription.add(isValidAll.subscribe { isValid -> mLogin!!.isEnabled = isValid!! })

        return view
    }

    override fun onStart() {
        super.onStart()
        //        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        //        EventBus.getDefault().unregister(this);
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
        compositeSubscription.unsubscribe()
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * Listener methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.login_btn)
    fun onClickButton(view: View) {
        if (BuildConfig.DEBUG && (mAccount!!.text == null || mAccount!!.text.length <= 0)) {
            // デバッグ用
            mAuthService!!.signIn("haijima", "password1!")
        } else {
            mAuthService!!.signIn(mAccount!!.text.toString(), mPassword!!.text.toString())
            ViewUtils.hideKeyboard(activity!!)
        }
    }

    @OnClick(R.id.line_login_btn)
    fun lineLogin(view: View) {
        val sdkContext = LineSdkContextManager.getSdkContext()
        val authManager = sdkContext.authManager
        val loginFuture = authManager.login(activity)
        loginFuture.addFutureListener { future ->
            when (future.progress) {
                LineLoginFuture.ProgressOfLogin.SUCCESS -> {
                    val mid = future.accessToken.mid
                    var md5Id = ""
                    try {
                        val str_bytes = "$mid!rhd2014".toByteArray(charset("UTF-8"))
                        var md: MessageDigest? = null
                        md = MessageDigest.getInstance("MD5")
                        val md5_bytes = md!!.digest(str_bytes)
                        val big_int = BigInteger(1, md5_bytes)
                        md5Id = big_int.toString(16)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        EventBus.getDefault().post(ShowSnackbarEvent("内部的なエラーが発生しました。もう一度お試しください"))
                        break
                    }

                    mAuthService!!.signInWithOAuth(mid, 1, md5Id)
                            .subscribe({ response ->
                                val user = response.body()
                                PreferenceUtil.putUserData(activity!!.applicationContext, user)
                                if (user.account == null) {
                                    val v = getLayoutInflater(null).inflate(R.layout.dialog_account_input, null)
                                    val accountEditText = ButterKnife.findById<EditText>(v, R.id.account)
                                    AlertDialog.Builder(activity!!)
                                            .setTitle("IDの登録")
                                            .setView(v)
                                            .setPositiveButton("決定") { dialog, which ->
                                                if (TextUtils.isEmpty(accountEditText.text)) {
                                                    return@new AlertDialog.Builder(activity)
                                                            .setTitle("IDの登録")
                                                            .setView(v)
                                                            .setPositiveButton
                                                }
                                                mUserService!!.setAccountToOAuthUser(user.id, accountEditText.text.toString())
                                                        .subscribe { res ->
                                                            if (res.isSuccess) {
                                                                EventBus.getDefault().post(ActivityTransitionEvent(MainActivity::class.java, false))
                                                            } else {
                                                                PreferenceUtil.clearUserData(activity!!.applicationContext)
                                                                EventBus.getDefault().post(ShowSnackbarEvent(ApiUtil.getApiError(res).detailedMessage))
                                                            }
                                                        }
                                            }.setCancelable(false)
                                            .create().show()
                                } else {
                                    EventBus.getDefault().post(ActivityTransitionEvent(MainActivity::class.java, false))
                                }
                            }, { t ->
                                t.printStackTrace()
                                EventBus.getDefault().post(ShowSnackbarEvent("ログイン失敗"))
                            })
                }
                LineLoginFuture.ProgressOfLogin.FAILED -> {
                    Log.e("!!!", "FAILED")
                    val failedCause = future.cause
                    if (failedCause is LineSdkLoginException) {
                        val error = failedCause.error
                        when (error) {
                            LineSdkLoginError.FAILED_START_LOGIN_ACTIVITY -> Log.e("!!!", "FAILED_START_LOGIN_ACTIVITY")
                            LineSdkLoginError.FAILED_A2A_LOGIN -> {
                            }
                            LineSdkLoginError.FAILED_WEB_LOGIN -> Log.e("!!!", "FAILED_WEB_LOGIN")
                            LineSdkLoginError.UNKNOWN -> Log.e("!!!", "UNKNOWN")
                        }// Failed launching LINE application or WebLoginActivity (Activity may be null)
                        // Failed LINE login
                        // Failed Web login
                        // Un expected error occurred
                    } else {
                        // Check other exceptions
                        Log.e("!!!", "other exception")
                    }
                }
                LineLoginFuture.ProgressOfLogin.CANCELED -> {
                }
                else -> {
                }
            }//                    sdkContext.getApiClient().getMyProfile(profileFuture -> {
            //                        switch (profileFuture.getStatus()) {
            //                            case SUCCESS:
            //                                Profile profile = profileFuture.getResponseObject();
            //                                String mid = profile.mid;
            //                                String displayName = profile.displayName;
            //                                String pictureUrl = profile.pictureUrl;
            //                                String statusMessage = profile.statusMessage;
            //                                break;
            //                            case FAILED:
            //                            default:
            //                                Throwable cause = profileFuture.getCause();
            //                                break;
            //                        }
            //                    });
        }
    }

    @OnClick(R.id.go_to_sign_up)
    fun goToSignUp(view: View) {
        EventBus.getDefault().post(FragmentTransitionEvent(SignUpFragment()))
    }

    @OnClick(R.id.forget)
    fun forgetAccoutnOrPassword(view: View) {
        val dialogView = activity!!.layoutInflater.inflate(R.layout.dialog_forget_account_or_password, null)
        AlertDialog.Builder(activity!!)
                .setTitle("アカウント再設定")
                .setView(dialogView)
                .setPositiveButton("送信") { dialog, id ->
                    mAuthService!!
                            .resetPassword(
                                    (dialogView.findViewById<View>(R.id.token) as EditText).text.toString(),
                                    (dialogView.findViewById<View>(R.id.new_password) as EditText).text.toString(),
                                    (dialogView.findViewById<View>(R.id.new_password_confirm) as EditText).text.toString())
                            .subscribe { response ->
                                if (response.isSuccess) {
                                    PreferenceUtil.putUserData(activity!!.applicationContext, response.body())
                                    EventBus.getDefault().post(ActivityTransitionEvent(MainActivity::class.java, false))
                                } else {
                                    EventBus.getDefault().post(ErrorEvent("アカウント再設定失敗", ApiUtil.getApiError(response).message))
                                }
                            }
                }
                .setNeutralButton("認証キー発行") { dialog1, which ->
                    val nextDialogView = activity!!.layoutInflater.inflate(R.layout.dialog_generate_reset_token, null)
                    AlertDialog.Builder(activity!!)
                            .setTitle("認証キー発行")
                            .setView(nextDialogView)
                            .setPositiveButton("送信"
                            ) { dialog, id ->
                                mAuthService!!
                                        .forgetPassword((nextDialogView.findViewById<View>(R.id.email) as EditText).text.toString())
                                        .subscribe { response ->
                                            if (response.isSuccess) {
                                                EventBus.getDefault().post(ShowSnackbarEvent(response.body().message!!))
                                            } else {
                                                EventBus.getDefault().post(ErrorEvent("確認用メール送信失敗", ApiUtil.getApiError(response).message))
                                            }
                                        }
                            }.create().show()
                }.create().show()
    }

}
