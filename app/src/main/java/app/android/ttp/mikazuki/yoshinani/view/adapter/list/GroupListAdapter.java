package app.android.ttp.mikazuki.yoshinani.view.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.TotalModel;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class GroupListAdapter extends ArrayAdapter<GroupModel> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final Map<Integer, TotalModel> mTotals;

    public GroupListAdapter(Context context, List<GroupModel> groups, Map<Integer, TotalModel> totals) {
        super(context, 0, groups);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTotals = totals;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_group, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GroupModel group = getItem(position);
        holder.name.setText(group.getName().get());
        TotalModel total = mTotals.get(group.getId());
        int amount = total != null ? total.getResult() : 0;
        holder.amount.setText(String.format("¥%,d", amount));
        holder.amount.setTextColor(mContext.getResources().getColor(amount >= 0 ? R.color.theme600 : R.color.red800));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.amount)
        TextView amount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}