package app.android.ttp.mikazuki.yoshinani.view.fragment

import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.databinding.FragmentPostRepaymentBinding
import app.android.ttp.mikazuki.yoshinani.event.DateSetEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent
import app.android.ttp.mikazuki.yoshinani.event.UserSelectEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.services.PaymentService
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.DatePickerDialogFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserSelectDialogFragment
import app.android.ttp.mikazuki.yoshinani.viewModel.PostRepaymentViewModel
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.jakewharton.rxbinding.widget.RxTextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels
import rx.Observable
import rx.subscriptions.CompositeSubscription

class PostRepaymentFragment : PostFragment() {
    private val TAG = PostPaymentFragment::class.java!!.getSimpleName()
    @BindView(R.id.amount)
    internal var mAmount: EditText? = null
    @BindView(R.id.participants)
    internal var mParticipantsBtn: Button? = null
    @BindView(R.id.post)
    internal var mPost: Button? = null
    private var mUnbinder: Unbinder? = null

    internal lateinit var binding: FragmentPostRepaymentBinding
    private var mUserService: UserService? = null
    private var mPaymentService: PaymentService? = null

    private var mViewModel: PostRepaymentViewModel? = null
    private var mGroupModel: GroupModel? = null
    private var mAllUserModels: List<UserModel>? = null
    private val mParticipantId: Int = 0

    private val compositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post_repayment, container, false)
        mUnbinder = ButterKnife.bind(this, view)

        mGroupModel = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))

        mUserService = UserService(activity!!.applicationContext)
        mPaymentService = PaymentService(activity!!.applicationContext)

        // TextInputLayoutのhintアニメーションを削除
        ViewUtils.disableTextInputLayoutHint(view, R.id.amount_input_layout)

        mUserService!!.getAll(mGroupModel!!.id)

        // バリデーション
        val isAmountCompleted = RxTextView.textChanges(mAmount!!).map { str -> !TextUtils.isEmpty(str) }
        val isParticipantsSelected = RxTextView.textChanges(mParticipantsBtn!!).map { str -> "返済相手選択" != str.toString() }
        val isValidAll = Observable.combineLatest(isAmountCompleted, isParticipantsSelected) { a, p -> a!! && p!! }
        compositeSubscription.add(isValidAll.subscribe { isValid -> mPost!!.isEnabled = isValid!! })

        return view
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostRepaymentBinding.bind(view)

        mViewModel = PostRepaymentViewModel(activity!!.applicationContext, mGroupModel!!.id)
        binding.viewModel = mViewModel
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.date)
    fun onDateSelect(v: View) {
        val dialogFragment = DatePickerDialogFragment()
        dialogFragment.setTargetFragment(this@PostRepaymentFragment, DATE_DIALOG_ID)
        val bundle = Bundle()
        bundle.putSerializable("date", mViewModel!!.date)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentManager!!, "datePicker")
    }

    @OnClick(R.id.participants)
    fun onUserSelect(v: View) {
        val dialogFragment = UserSelectDialogFragment()
        val bundle = Bundle()
        val users = arrayOfNulls<CharSequence>(mAllUserModels!!.size)
        for (i in users.indices) {
            users[i] = mAllUserModels!![i].displayName
        }
        bundle.putCharSequenceArray("users", users)
        bundle.putInt("selected", mViewModel!!.participantsId)
        dialogFragment.arguments = bundle
        dialogFragment.setTargetFragment(this@PostRepaymentFragment, USER_SELECT)
        dialogFragment.show(fragmentManager!!, "userSelect")
    }

    @OnClick(R.id.post)
    fun onPostPayment(v: View) {
        val uid = Integer.parseInt(PreferenceUtil.getUid(activity!!.applicationContext)!!)
        val me = UserModel()
        me.id = uid
        mPaymentService!!.create(mViewModel!!.model)
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: DateSetEvent) {
        mViewModel!!.date = event.date
    }

    @Subscribe
    fun onEvent(event: UserSelectEvent) {
        mViewModel!!.setParticipants(event.value)
    }

    @Subscribe
    fun onEvent(event: FetchListDataEvent<UserModel>) {
        mAllUserModels = event.listData
        mViewModel!!.setAllUsers(mAllUserModels ?: arrayListOf())
    }

    @Subscribe
    fun onEvent(event: FetchDataEvent<PaymentModel>) {
        // 初期化
        mViewModel!!.reset()
        activity!!.onBackPressed()
    }

    companion object {

        private val USER_SELECT = 111
        private val DATE_DIALOG_ID = 999
    }

}
