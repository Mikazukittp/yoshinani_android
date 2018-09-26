package app.android.ttp.mikazuki.yoshinani.view.adapter.list

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.model.TotalModel
import app.android.ttp.mikazuki.yoshinani.model.UserModel
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils
import butterknife.BindView
import butterknife.ButterKnife
import rx.Observable

/**
 * @author haijimakazuki
 */
class MemberListAdapter(private val mContext: Context, userModels: List<UserModel>, private val mGroupModel: GroupModel) : ArrayAdapter<UserModel>(mContext, 0, userModels) {
    private val layoutInflater: LayoutInflater

    init {
        this.layoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val holder: ViewHolder

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_member, parent, false)
            holder = ViewHolder(convertView)
            convertView!!.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }

        val user = getItem(position)
        holder.icon!!.setImageDrawable(user!!.icon)
        holder.userName!!.text = user.displayName
        val amount = Observable.from(user.totals)
                .filter { total -> total.groupId == mGroupModel.id }
                .map<Int>(Func1<TotalModel, Int> { it.getResult() })
                .defaultIfEmpty(0)
                .toBlocking().single()
        holder.userAmount!!.text = TextUtils.wrapCurrency(amount.toDouble())
        holder.userAmount!!.setTextColor(mContext.resources.getColor(if (amount >= 0) R.color.theme600 else R.color.red800))

        return convertView
    }

    internal class ViewHolder(view: View) {
        @BindView(R.id.icon)
        var icon: ImageView? = null
        @BindView(R.id.userName)
        var userName: TextView? = null
        @BindView(R.id.userAmount)
        var userAmount: TextView? = null

        init {
            ButterKnife.bind(this, view)
        }
    }

    companion object {
        private val TAG = MemberListAdapter::class.java!!.getSimpleName()
    }

}
