package app.android.ttp.mikazuki.yoshinani.view.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.services.GroupService;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * @author haijimakazuki
 */
public class InvitedGroupListAdapter extends ArrayAdapter<GroupModel> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private GroupService mGroupService;

    public InvitedGroupListAdapter(Context context, List<GroupModel> invitedGroups) {
        super(context, 0, invitedGroups);
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupService = new GroupService(mContext);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_invited_group, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GroupModel group = getItem(position);
        holder.name.setText(group.getName().get());
        holder.description.setText(group.getDescription().get());
        holder.accept.setOnClickListener(v -> mGroupService.accept(group.getId()));
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.description)
        TextView description;
        @Bind(R.id.accept)
        Button accept;
        @Bind(R.id.decline)
        Button decline;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}