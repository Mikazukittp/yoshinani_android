package app.android.ttp.mikazuki.yoshinani.view.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.binding.BindableString;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.TotalModel;
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class GroupListAdapter extends ArrayAdapter<GroupListAdapter.GroupListItem> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;

    public GroupListAdapter(Context context,
                            List<GroupListItem> groups) {
        super(context, 0, groups);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static List<GroupListItem> createGroupListItems(List<GroupModel> activeGroups,
                                                           List<GroupModel> invitedGroups,
                                                           Map<Integer, TotalModel> totals) {
        List<GroupListItem> items = new ArrayList<>();
        for (GroupModel group : activeGroups) {
            items.add(new GroupListItem(group, totals.get(group.getId()), true));
        }
        for (GroupModel group : invitedGroups) {
            items.add(new GroupListItem(group, null, false));
        }
        return items;
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

        GroupListItem group = getItem(position);
        holder.name.setText(group.getName().get());
        if (group.mIsActive) {
            holder.name.setTextColor(mContext.getResources().getColor(R.color.grey800));
            holder.amount.setText(TextUtils.wrapCurrency(group.getTotalResult()));
            holder.amount.setTextColor(mContext.getResources().getColor(group.getTotalResult() >= 0 ? R.color.theme600 : R.color.red800));
        } else {
            holder.name.setTextColor(mContext.getResources().getColor(R.color.grey400));
            holder.amount.setText("招待中");
            holder.amount.setTextColor(mContext.getResources().getColor(R.color.grey400));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.amount)
        TextView amount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public static class GroupListItem {

        private GroupModel mGroup;
        private int mTotalResult;
        private boolean mIsActive;

        public GroupListItem(GroupModel group, TotalModel total, boolean isActive) {
            mGroup = group;
            mTotalResult = total != null ? total.getResult() : 0;
            mIsActive = isActive;
        }

        public BindableString getName() {
            return mGroup.getName();
        }

        public int getTotalResult() {
            return mTotalResult;
        }

    }
}