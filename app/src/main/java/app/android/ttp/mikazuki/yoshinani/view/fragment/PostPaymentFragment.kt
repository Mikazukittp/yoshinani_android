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
import app.android.ttp.mikazuki.yoshinani.databinding.FragmentPostPaymentBinding
import app.android.ttp.mikazuki.yoshinani.event.DateSetEvent
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.event.UserMultiSelectEvent
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.services.PaymentService
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.utils.ViewUtils
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.DatePickerDialogFragment
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.UserMultiSelectDialogFragment
import app.android.ttp.mikazuki.yoshinani.viewModel.PostPaymentViewModel
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.Unbinder
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.jakewharton.rxbinding.widget.RxTextView
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.parceler.Parcels
import rx.Observable
import rx.subscriptions.CompositeSubscription
import java.util.*

class PostPaymentFragment : PostFragment() {
    @BindView(R.id.amount)
    internal var mAmount: EditText? = null
    @BindView(R.id.event)
    internal var mEvent: EditText? = null
    @BindView(R.id.description)
    internal var mDescription: EditText? = null
    @BindView(R.id.date)
    internal var mDateBtn: Button? = null
    @BindView(R.id.participants)
    internal var mParticipantsBtn: Button? = null
    @BindView(R.id.post)
    internal var mPost: Button? = null
    private var mUnbinder: Unbinder? = null

    internal lateinit var binding: FragmentPostPaymentBinding
    internal var mInterstitialAd: InterstitialAd? = null
    private var mUserService: UserService? = null
    private var mPaymentService: PaymentService? = null
    private var mViewModel: PostPaymentViewModel? = null
    private var mGroupModel: GroupModel? = null
    private var mAllUserModels: List<UserModel>? = null

    private val mParticipantsIds: ArrayList<Int>? = null

    private val compositeSubscription = CompositeSubscription()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_post_payment, container, false)
        mUnbinder = ButterKnife.bind(this, view)

        mGroupModel = Parcels.unwrap<GroupModel>(arguments!!.getParcelable<Parcelable>(Constants.BUNDLE_GROUP_KEY))

        mUserService = UserService(activity!!.applicationContext)
        mPaymentService = PaymentService(activity!!.applicationContext)

        // TextInputLayoutのhintアニメーションを削除
        ViewUtils.disableTextInputLayoutHint(view, R.id.amount_input_layout)
        ViewUtils.disableTextInputLayoutHint(view, R.id.event_input_layout)
        ViewUtils.disableTextInputLayoutHint(view, R.id.description_input_layout)

        mUserService!!.getAll(mGroupModel!!.id)

        // 全画面広告の準備
        requestNewInterstitial()

        // バリデーション
        val isAmountCompleted = RxTextView.textChanges(mAmount!!).map { str -> !TextUtils.isEmpty(str) }
        val isEventCompleted = RxTextView.textChanges(mEvent!!).map { str -> !TextUtils.isEmpty(str) }
        val isDescriptionCompleted = RxTextView.textChanges(mDescription!!).map { str -> !TextUtils.isEmpty(str) }
        val isParticipantsSelected = RxTextView.textChanges(mParticipantsBtn!!).map { str -> "参加者選択" != str.toString() }
        val isValidAll = Observable.combineLatest(isAmountCompleted, isEventCompleted, isDescriptionCompleted, isParticipantsSelected) { a, e, d, p -> a!! && e!! && d!! && p!! }
        compositeSubscription.add(isValidAll.subscribe { isValid -> mPost!!.isEnabled = isValid!! })

        return view
    }

    override fun onViewCreated(view: View,
                               savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostPaymentBinding.bind(view)

        mViewModel = PostPaymentViewModel(activity!!.applicationContext, mGroupModel!!.id)
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
        compositeSubscription.unsubscribe()
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    private fun requestNewInterstitial() {
        if (mInterstitialAd == null) {
            mInterstitialAd = InterstitialAd(activity!!.applicationContext)
            mInterstitialAd!!.adUnitId = getString(R.string.interstitial_ad_unit_id)
            mInterstitialAd!!.adListener = object : AdListener() {
                override fun onAdClosed() {
                    requestNewInterstitial()
                    //                    getActivity().onBackPressed();
                }
            }
        }
        val adRequest = AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device_id))
                .build()
        mInterstitialAd!!.loadAd(adRequest)
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @OnClick(R.id.date)
    fun onDateSelect(v: View) {
        val dialogFragment = DatePickerDialogFragment()
        dialogFragment.setTargetFragment(this@PostPaymentFragment, DATE_DIALOG_ID)
        val bundle = Bundle()
        bundle.putSerializable("date", mViewModel!!.date)
        dialogFragment.arguments = bundle
        dialogFragment.show(fragmentManager!!, "datePicker")
    }

    @OnClick(R.id.participants)
    fun onUserSelect(v: View) {
        val dialogFragment = UserMultiSelectDialogFragment()
        val bundle = Bundle()
        val users = arrayOfNulls<CharSequence>(mAllUserModels!!.size)
        for (i in users.indices) {
            users[i] = mAllUserModels!![i].displayName
        }
        bundle.putCharSequenceArray("users", users)
        bundle.putIntegerArrayList("selected", mViewModel!!.participantsIdArray)
        dialogFragment.arguments = bundle
        dialogFragment.setTargetFragment(this@PostPaymentFragment, USER_SELECT)
        dialogFragment.show(fragmentManager!!, "userSelect")
    }

    @OnClick(R.id.post)
    fun onPostPayment(v: View) {
        mPaymentService!!.create(mViewModel!!.model)
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    fun onEvent(event: DateSetEvent) {
        mViewModel!!.date = event.date
    }

    @Subscribe
    fun onEvent(event: UserMultiSelectEvent) {
        mViewModel!!.setParticipants(event.value)
    }

    @Subscribe
    fun onEvent(event: FetchListDataEvent<UserModel>) {
        mAllUserModels = event.listData
        mViewModel!!.setAllUsers(mAllUserModels ?: arrayListOf())
    }

    @Subscribe
    fun onEvent(event: RefreshEvent) {
        mViewModel!!.reset()
        if (mInterstitialAd!!.isLoaded && Math.random() < 0.2) {
            mInterstitialAd!!.show()
        } else {
            activity!!.onBackPressed()
        }
    }

    companion object {

        private val USER_SELECT = 111
        private val DATE_DIALOG_ID = 999
    }

}
