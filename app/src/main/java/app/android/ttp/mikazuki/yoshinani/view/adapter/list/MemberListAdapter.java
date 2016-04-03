package app.android.ttp.mikazuki.yoshinani.view.adapter.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.TotalModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils;
import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import rx.Observable;

/**
 * @author haijimakazuki
 */
public class MemberListAdapter extends ArrayAdapter<UserModel> {
    private static final String TAG = MemberListAdapter.class.getSimpleName();
    private final GroupModel mGroupModel;
    private LayoutInflater layoutInflater;
    private Context mContext;

    public MemberListAdapter(Context context, List<UserModel> userModels, GroupModel groupModel) {
        super(context, 0, userModels);
        mContext = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mGroupModel = groupModel;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_member, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        UserModel user = getItem(position);
//        holder.icon.setImageDrawable(user.getIcon());
        Glide.with(mContext)
                .load(user.getIconUrl())
                .override(160, 160)
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(mContext))
                .thumbnail(0.5f)
                .placeholder(user.getIcon())
                .error(user.getIcon())
                .into(holder.icon);
        holder.userName.setText(user.getDisplayName());
        int amount = Observable.from(user.getTotals())
                .filter(total -> total.getGroupId() == mGroupModel.getId())
                .map(TotalModel::getResult)
                .defaultIfEmpty(0)
                .toBlocking().single();
        holder.userAmount.setText(TextUtils.wrapCurrency(amount));
        holder.userAmount.setTextColor(mContext.getResources().getColor(amount >= 0 ? R.color.theme600 : R.color.red800));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.icon)
        ImageView icon;
        @Bind(R.id.userName)
        TextView userName;
        @Bind(R.id.userAmount)
        TextView userAmount;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
