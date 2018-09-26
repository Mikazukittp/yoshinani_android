package app.android.ttp.mikazuki.yoshinani.view.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.services.GroupService
import butterknife.BindView
import butterknife.ButterKnife

/**
 * @author haijimakazuki
 */
class InvitedGroupListAdapter(private val mContext: Context, invitedGroups: List<GroupModel>) : ArrayAdapter<GroupModel>(mContext, 0, invitedGroups) {
    private val mLayoutInflater: LayoutInflater
    private val mGroupService: GroupService

    init {
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mGroupService = GroupService(mContext)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_invited_group, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val group = getItem(position)
        holder.name!!.text = group!!.name.get()
        holder.description!!.text = group.description.get()
        holder.accept!!.setOnClickListener { v -> mGroupService.accept(group.id) }
        return convertView
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.name)
        var name: TextView? = null
        @BindView(R.id.description)
        var description: TextView? = null
        @BindView(R.id.accept)
        var accept: Button? = null
        @BindView(R.id.decline)
        var decline: Button? = null

        init {
            ButterKnife.bind(this, view)
        }
    }
}