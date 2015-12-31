package app.android.ttp.mikazuki.yoshinani.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class UserListAdapter extends ArrayAdapter<User> {
    private static final String TAG = "UserListAdapter";
    private LayoutInflater layoutInflater;

    public UserListAdapter(Context c, int id, List<User> users) {
        super(c, id, users);
        this.layoutInflater = (LayoutInflater) c.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE
        );
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.overview_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        User user = getItem(position);
        holder.userName.setText(user.getUsername());
         int amount = (int)Math.round(user.getTotals().get(0).getPaid() - user.getTotals().get(0).getToPay());
//         int amount = 0;
        holder.userAmount.setText("￥"+Integer.toString(amount));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.userName)
        TextView userName;
        @Bind(R.id.userAmount)
        TextView userAmount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
