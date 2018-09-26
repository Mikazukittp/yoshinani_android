package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.UnauthorizedEvent
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.view.activity.EditProfileActivity
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import org.greenrobot.eventbus.EventBus

/**
 * @author haijimakazuki
 */
class AccountSettingFragment : Fragment() {

    internal lateinit var mUserService: UserService

    private var mUnbinder: Unbinder? = null

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_account_setting, container, false)
        mUnbinder = ButterKnife.bind(this, view)
        mUserService = UserService(activity!!.applicationContext)
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar!!.title = getString(R.string.menu_account)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.edit_profile)
    fun editProfile(v: View) {
        val i = Intent(activity, EditProfileActivity::class.java)
        i.putExtras(arguments!!)
        startActivity(i)
    }

    @OnClick(R.id.logout)
    fun logout(v: View) {
        val builder = AlertDialog.Builder(activity!!)
        builder.setMessage("本当にログアウトしますか？")
                .setPositiveButton("はい") { dialog, id ->
                    //                    mUserService.deleteToken(PreferenceUtil.getNotificationToken(getActivity().getApplicationContext()));
                    PreferenceUtil.clearUserData(activity!!.applicationContext)
                    EventBus.getDefault().post(UnauthorizedEvent())
                }
                .setNegativeButton("いいえ") { dialog, id -> }
        builder.create().show()
    }

}
