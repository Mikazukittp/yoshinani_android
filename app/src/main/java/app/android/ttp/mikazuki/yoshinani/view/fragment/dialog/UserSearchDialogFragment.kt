package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.services.GroupService
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels

/**
 * @author haijimakazuki
 */
class UserSearchDialogFragment : DialogFragment() {

    @BindView(R.id.account)
    internal var mAccount: EditText? = null
    @BindView(R.id.search_result)
    internal var mSearchResult: LinearLayout? = null
    @BindView(R.id.found_account)
    internal var mFoundAccount: TextView? = null
    @BindView(R.id.found_username)
    internal var mFoundUsername: TextView? = null
    @BindView(R.id.search_btn)
    internal var mSearchBtn: Button? = null

    private var mGroup: GroupModel? = null
    private var mUser: UserModel? = null
    private var mUserService: UserService? = null
    private var mGroupService: GroupService? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_user_search, null, false)
        ButterKnife.bind(this, view)

        mGroup = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))

        mUserService = UserService(activity!!.applicationContext)
        mGroupService = GroupService(activity!!.applicationContext)
        val builder = AlertDialog.Builder(activity!!)
                .setTitle("")
                .setView(view)
                .setPositiveButton("招待") { dialog, which ->
                    if (mUser != null) {
                        mGroupService!!.invite(mGroup!!.id, mUser!!.id)
                    }
                }
                .setNegativeButton("キャンセル") { dialog, which -> dismiss() }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        EventBus.getDefault().unregister(this)
        super.onPause()
    }

    @OnClick(R.id.search_btn)
    fun searchUser(view: View) {
        if (mAccount!!.text.length > 0) {
            mUserService!!.search(mAccount!!.text.toString())
        }
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: FetchDataEvent<UserModel>) {
        mUser = event.data
        if (mSearchResult!!.visibility == View.GONE) {
            mSearchResult!!.visibility = View.VISIBLE
        }

        mFoundAccount!!.text = mUser!!.account
        mFoundUsername!!.text = mUser!!.username
    }

}