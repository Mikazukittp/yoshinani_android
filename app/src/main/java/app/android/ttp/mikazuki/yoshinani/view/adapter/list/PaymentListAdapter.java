package app.android.ttp.mikazuki.yoshinani.view.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.databinding.ListPaymentBinding;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;

/**
 * Created by ishibetatsuya on 15/07/10.
 */
public class PaymentListAdapter extends ArrayAdapter<PaymentModel> {

    private final Context mContext;
    private LayoutInflater layoutInflater;

    public PaymentListAdapter(Context context, List<PaymentModel> payments) {
        super(context, 0, payments);
        mContext = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListPaymentBinding binding;
        if (convertView == null) {
            binding = ListPaymentBinding.inflate(LayoutInflater.from(getContext()));
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            binding = (ListPaymentBinding) convertView.getTag();
        }
        PaymentModel payment = getItem(position);
        binding.setPayment(payment);
//        binding.name.setText("@" + payment.getPaidUser().getAccount());
//        binding.price.setText(String.format("¥%,d", payment.getAmount().get()));
//        binding.eventDescription.setText(payment.getEvent().get() + ":" + payment.getDescription().get());
//        binding.date.setText(ModelUtils.formatDate(payment.getDate()));
        return convertView;
    }
}
