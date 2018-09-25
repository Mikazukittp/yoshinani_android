package app.android.ttp.mikazuki.yoshinani.view.fragment.dialog;

import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.DialogPaymentDetailBinding;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.services.PaymentService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author haijimakazuki
 */
public class PaymentDetailDialogFragment extends DialogFragment {

    @BindView(R.id.participants_list)
    LinearLayout mParticipantsList;
    private Unbinder mUnbinder;

    private PaymentService mPaymentService;

    public PaymentDetailDialogFragment() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_payment_detail, null, false);
        mUnbinder = ButterKnife.bind(this, view);

        getContext();
        mPaymentService = new PaymentService(getActivity().getApplicationContext());

        final DialogPaymentDetailBinding binding = DialogPaymentDetailBinding.bind(view);
        final PaymentModel payment = Parcels.unwrap(getArguments().getParcelable("payment"));
        binding.setPayment(payment);

        // 参加者リストのレイアウトを動的に作成
        final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout ll = (LinearLayout) view.findViewById(R.id.participants_list);
        for (UserModel participant : payment.getParticipants()) {
            final TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setText(participant.getDisplayName());
            textView.setTextColor(getResources().getColor(R.color.grey600));
            textView.setTypeface(Typeface.DEFAULT_BOLD);
            textView.setTextSize(16);
            mParticipantsList.addView(textView, param);
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.detail))
                .setView(view)
                .setPositiveButton("はい", (dialog, id) -> {
                })
                .setNegativeButton("閉じる", (dialog, id) -> {
                });
        if (payment.paid(getActivity().getApplicationContext())) {
            builder.setNeutralButton("削除", ((dialog, id) -> {
                mPaymentService.delete(payment.getId());
                EventBus.getDefault().post(new RefreshEvent());
            }));
        }
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }
}