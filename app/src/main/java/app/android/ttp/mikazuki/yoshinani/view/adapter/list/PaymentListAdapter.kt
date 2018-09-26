package app.android.ttp.mikazuki.yoshinani.view.adapter.list

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.databinding.ListPaymentBinding
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel
import butterknife.BindView
import butterknife.ButterKnife

/**
 * 参考: http://msobhy.me/2015/09/05/infinite_scrolling_recyclerview/
 *
 * @author haijimakazuki
 */
class PaymentListAdapter(recyclerView: RecyclerView,
                         private val mItems: MutableList<PaymentModel>?,
                         private val mListener: OnRecyclerListener?,
                         private val mOnLoadMoreListener: OnLoadMoreListener?) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val mInflater: LayoutInflater

    private var mTotalItemCount: Int = 0
    private var mVisibleItemCount: Int = 0
    private var mFirstVisibleItem: Int = 0
    private var mPreviousTotal = 0
    private var mLoading = true

    init {
        mInflater = LayoutInflater.from(recyclerView.context)

        if (recyclerView.layoutManager is LinearLayoutManager) {
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    mTotalItemCount = linearLayoutManager!!.itemCount
                    mVisibleItemCount = linearLayoutManager.childCount
                    mFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition()

                    if (mLoading && mTotalItemCount > mPreviousTotal) {
                        mLoading = false
                        mPreviousTotal = mTotalItemCount
                    } else if (!mLoading && mTotalItemCount - mVisibleItemCount <= mFirstVisibleItem + VISIBLE_THRESHOLD) {
                        mLoading = true
                        addItem(null)
                        mOnLoadMoreListener?.onLoadMore()
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_VIEW_TYPE_BASIC) {
            ViewHolder(mInflater.inflate(R.layout.list_payment, viewGroup, false))
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            ProgressViewHolder(mInflater.inflate(R.layout.list_loading, viewGroup, false))
        } else {
            throw IllegalStateException("Invalid type, this type ot items $viewType can't be handled")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_BASIC) {
            (holder as ViewHolder).binding.payment = mItems!![position]
            holder.binding.executePendingBindings()
            if (mListener != null) {
                holder.itemView.setOnLongClickListener { v -> mListener.onItemLongClicked(v, position) }
            }
        } else {
            (holder as ProgressViewHolder).progressBar!!.isIndeterminate = true
        }
    }

    override fun getItemCount(): Int {
        return mItems?.size ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (mItems!![position] != null) ITEM_VIEW_TYPE_BASIC else ITEM_VIEW_TYPE_FOOTER
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    fun resetItems(payments: List<PaymentModel>) {
        mLoading = true
        mFirstVisibleItem = 0
        mVisibleItemCount = 0
        mTotalItemCount = 0
        mPreviousTotal = 0

        mItems!!.clear()
        addItems(payments)
    }

    fun addItems(newPayments: List<PaymentModel>) {
        for (p in newPayments) {
            addItem(p, false)
        }
        notifyDataSetChanged()
    }

    @JvmOverloads
    fun addItem(item: PaymentModel?, shouldNotifyChange: Boolean = true) {
        if (!mItems!!.contains(item)) {
            mItems.add(item)
            if (shouldNotifyChange) {
                notifyItemInserted(mItems.size - 1)
            }
        }
    }

    fun removeItem(item: PaymentModel) {
        var indexOfItem = mItems!!.indexOf(item)
        while (indexOfItem != -1) {
            this.mItems.removeAt(indexOfItem)
            notifyItemRemoved(indexOfItem)
            indexOfItem = mItems.indexOf(item)
        }
    }

    /**
     * ClickListener
     */
    interface OnRecyclerListener {
        fun onItemLongClicked(v: View, position: Int): Boolean
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * ViewHolders
     */
    /* ------------------------------------------------------------------------------------------ */
    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val binding: ListPaymentBinding

        init {
            //            mBinding = ListPaymentBinding.inflate(mInflater);
            binding = ListPaymentBinding.bind(v)
        }
    }

    class ProgressViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        @BindView(R.id.progress_bar)
        var progressBar: ProgressBar? = null

        init {
            ButterKnife.bind(this, v)
        }
    }

    companion object {
        private val VISIBLE_THRESHOLD = 5
        private val ITEM_VIEW_TYPE_BASIC = 0
        private val ITEM_VIEW_TYPE_FOOTER = 1
    }
}
