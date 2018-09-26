package app.android.ttp.mikazuki.yoshinani.view.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

import java.util.ArrayList

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.binding.BindableString
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.TotalModel
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils
import butterknife.BindView
import butterknife.ButterKnife

/**
 * @author haijimakazuki
 */
class GroupListAdapter(private val mContext: Context,
                       groups: List<GroupListItem>) : ArrayAdapter<GroupListAdapter.GroupListItem>(mContext, 0, groups) {
    private val mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_group, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val group = getItem(position)
        holder.name!!.text = group!!.name.get()
        if (group.mIsActive) {
            holder.name!!.setTextColor(mContext.resources.getColor(R.color.grey800))
            holder.amount!!.text = TextUtils.wrapCurrency(group.totalResult.toDouble())
            holder.amount!!.setTextColor(mContext.resources.getColor(if (group.totalResult >= 0) R.color.theme600 else R.color.red800))
        } else {
            holder.name!!.setTextColor(mContext.resources.getColor(R.color.grey400))
            holder.amount!!.text = "招待中"
            holder.amount!!.setTextColor(mContext.resources.getColor(R.color.grey400))
        }
        return convertView
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.name)
        var name: TextView? = null
        @BindView(R.id.amount)
        var amount: TextView? = null

        init {
            ButterKnife.bind(this, view)
        }
    }

    class GroupListItem(private val mGroup: GroupModel, total: TotalModel?, private val mIsActive: Boolean) {
        val totalResult: Int

        val name: BindableString
            get() = mGroup.name

        init {
            totalResult = total?.result ?: 0
        }

    }

    companion object {

        fun createGroupListItems(activeGroups: List<GroupModel>,
                                 invitedGroups: List<GroupModel>,
                                 totals: Map<Int, TotalModel>): List<GroupListItem> {
            val items = ArrayList<GroupListItem>()
            for (group in activeGroups) {
                items.add(GroupListItem(group, totals[group.id], true))
            }
            for (group in invitedGroups) {
                items.add(GroupListItem(group, null, false))
            }
            return items
        }
    }
}