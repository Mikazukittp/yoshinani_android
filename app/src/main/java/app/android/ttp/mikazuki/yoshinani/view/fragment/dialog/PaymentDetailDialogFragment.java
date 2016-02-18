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

import org.parceler.Parcels;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.DialogPaymentDetailBinding;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class PaymentDetailDialogFragment extends DialogFragment {

    @Bind(R.id.participants_list)
    LinearLayout mParticipantsList;

    private Bundle mBundle;

    public PaymentDetailDialogFragment() {
    }

    public void setBundle(Bundle bundle) {
        mBundle = bundle;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_payment_detail, null, false);
        ButterKnife.bind(this, view);

        final DialogPaymentDetailBinding binding = DialogPaymentDetailBinding.bind(view);
        final PaymentModel payment = Parcels.unwrap(mBundle.getParcelable("payment"));
        binding.setPayment(payment);

        // 参加者リストのレイアウトを動的に作成
        final LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final LinearLayout ll = (LinearLayout) view.findViewById(R.id.participants_list);
        for (UserModel participant : payment.getParticipants()) {
            final TextView textView = new TextView(getActivity().getApplicationContext());
            textView.setText(participant.getUsername());
            textView.setTextColor(getResources().getColor(R.color.gray600));
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
        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}