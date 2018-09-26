package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText

import com.jakewharton.rxbinding.widget.RxTextView

import org.greenrobot.eventbus.EventBus

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.FragmentTransitionEvent
import app.android.ttp.mikazuki.yoshinani.services.AuthService
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import rx.Observable
import rx.subscriptions.CompositeSubscription

/**
 * @author haijimakazuki
 */
class SignUpFragment : Fragment() {

    @BindView(R.id.account)
    internal var mAccount: EditText? = null
    @BindView(R.id.email)
    internal var mEmail: EditText? = null
    @BindView(R.id.password)
    internal var mPassword: EditText? = null
    @BindView(R.id.password_confirm)
    internal var mPasswordConfirm: EditText? = null

    @BindView(R.id.register_btn)
    internal var mRegister: Button? = null
    private var mUnbinder: Unbinder? = null

    private var mAuthService: AuthService? = null

    private val compositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)
        mUnbinder = ButterKnife.bind(this, view)

        mAuthService = AuthService(activity!!.applicationContext)

        // バリデーション
        val isAccountCompleted = RxTextView.textChanges(mAccount!!).map { str -> !TextUtils.isEmpty(str) }
        val isEmailCompleted = RxTextView.textChanges(mEmail!!).map { str -> !TextUtils.isEmpty(str) }
        val isPasswordCompleted = RxTextView.textChanges(mPassword!!).map { str -> !TextUtils.isEmpty(str) }
        val isPasswordConfirmCompleted = RxTextView.textChanges(mPasswordConfirm!!).map { str -> !TextUtils.isEmpty(str) }
        val isValidAll = Observable.combineLatest(isAccountCompleted, isEmailCompleted, isPasswordCompleted, isPasswordConfirmCompleted) { a, e, p, pc -> a!! && e!! && p!! && pc!! }
        compositeSubscription.add(isValidAll.subscribe { isValid -> mRegister!!.isEnabled = isValid!! })

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
    @OnClick(R.id.register_btn)
    fun onClickButton(view: View) {
        mAuthService!!.signUp(mAccount!!.text.toString(), mEmail!!.text.toString(), mPassword!!.text.toString())
        val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(null, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    @OnClick(R.id.go_to_sign_in)
    fun goToSignUp(view: View) {
        EventBus.getDefault().post(FragmentTransitionEvent(SignInFragment()))
    }

}
