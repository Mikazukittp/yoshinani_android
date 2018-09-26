package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog

import android.app.Dialog
import android.graphics.Typeface
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.databinding.DialogPaymentDetailBinding
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import app.android.ttp.mikazuki.yoshinani.services.PaymentService
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import org.greenrobot.eventbus.EventBus
import org.parceler.Parcels

/**
 * @author haijimakazuki
 */
class PaymentDetailDialogFragment : DialogFragment() {

    @BindView(R.id.participants_list)
    internal var mParticipantsList: LinearLayout? = null
    private var mUnbinder: Unbinder? = null

    private var mPaymentService: PaymentService? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity!!.layoutInflater.inflate(R.layout.dialog_payment_detail, null, false)
        mUnbinder = ButterKnife.bind(this, view)

        context
        mPaymentService = PaymentService(activity!!.applicationContext)

        val binding = DialogPaymentDetailBinding.bind(view)
        val payment = Parcels.unwrap<PaymentModel>(arguments!!.getParcelable<Parcelable>("payment"))
        binding.payment = payment

        // 参加者リストのレイアウトを動的に作成
        val param = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val ll = view.findViewById<View>(R.id.participants_list) as LinearLayout
        for (participant in payment.participants) {
            val textView = TextView(activity!!.applicationContext)
            textView.text = participant.displayName
            textView.setTextColor(resources.getColor(R.color.grey600))
            textView.typeface = Typeface.DEFAULT_BOLD
            textView.textSize = 16f
            mParticipantsList!!.addView(textView, param)
        }

        val builder = AlertDialog.Builder(activity!!)
                .setMessage(getString(R.string.detail))
                .setView(view)
                .setPositiveButton("はい") { dialog, id -> }
                .setNegativeButton("閉じる") { dialog, id -> }
        if (payment.paid(activity!!.applicationContext)) {
            builder.setNeutralButton("削除") { dialog, id ->
                mPaymentService!!.delete(payment.id)
                EventBus.getDefault().post(RefreshEvent())
            }
        }
        return builder.create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mUnbinder!!.unbind()
    }
}