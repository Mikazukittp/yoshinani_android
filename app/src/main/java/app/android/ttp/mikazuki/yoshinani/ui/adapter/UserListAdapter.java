package app.android.ttp.mikazuki.yoshinani.ui.adapter;

import android.content.Context;
import android.util.Log;
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
        Log.d(TAG, "==========");
        Log.d(TAG, user.get_id());
        Log.d(TAG, user.getEmail());
        Log.d(TAG, user.getName());
        Log.d(TAG, user.getProvider());
        Log.d(TAG, user.getRole());
        Log.d(TAG, user.getCurrentHaveToPay()+"");
        Log.d(TAG, user.getCurrentPaid()+"");
        Log.d(TAG, "==========");
        holder.userName.setText(user.getName());
        int amount = user.getCurrentPaid() - user.getCurrentHaveToPay();
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
