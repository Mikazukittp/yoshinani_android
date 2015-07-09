package app.android.ttp.mikazuki.yoshinani.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Choice;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Question;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class PaymentListAdapter extends ArrayAdapter<Payment> {
    private LayoutInflater layoutInflater;

    public PaymentListAdapter(Context c, int id, List<Payment> payments) {
        super(c, id, payments);
        this.layoutInflater = (LayoutInflater) c.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Payment payment = getItem(position);
        holder.question.setText(payment.getAmount());
        StringBuilder sb = new StringBuilder();
//        for (Choice choice : question.getChoices()) {
//            sb.append(choice.getChoice());
//            sb.append("(");
//            sb.append(choice.getVotes());
//            sb.append(") ");
//        }
//        holder.choices.setText(new String(sb));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.question)
        TextView question;
        @Bind(R.id.choices)
        TextView choices;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
