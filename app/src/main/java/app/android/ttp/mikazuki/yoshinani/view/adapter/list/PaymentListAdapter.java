package app.android.ttp.mikazuki.yoshinani.view.adapter.list;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.databinding.ListPaymentBinding;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 参考: http://msobhy.me/2015/09/05/infinite_scrolling_recyclerview/
 *
 * @author haijimakazuki
 */
public class PaymentListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VISIBLE_THRESHOLD = 5;
    private static final int ITEM_VIEW_TYPE_BASIC = 0;
    private static final int ITEM_VIEW_TYPE_FOOTER = 1;
    private LayoutInflater mInflater;
    private List<PaymentModel> mItems;
    private OnRecyclerListener mListener;

    private int mTotalItemCount, mVisibleItemCount, mFirstVisibleItem, mPreviousTotal = 0;
    private boolean mLoading = true;
    private OnLoadMoreListener mOnLoadMoreListener;

    public PaymentListAdapter(@NonNull final RecyclerView recyclerView,
                              @NonNull final List<PaymentModel> payments,
                              @Nullable final OnRecyclerListener listener,
                              @Nullable final OnLoadMoreListener onLoadMoreListener) {
        mInflater = LayoutInflater.from(recyclerView.getContext());
        mItems = payments;
        mListener = listener;
        mOnLoadMoreListener = onLoadMoreListener;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    mTotalItemCount = linearLayoutManager.getItemCount();
                    mVisibleItemCount = linearLayoutManager.getChildCount();
                    mFirstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();

                    if (mLoading && mTotalItemCount > mPreviousTotal) {
                        mLoading = false;
                        mPreviousTotal = mTotalItemCount;
                    } else if (!mLoading && (mTotalItemCount - mVisibleItemCount) <= (mFirstVisibleItem + VISIBLE_THRESHOLD)) {
                        mLoading = true;
                        addItem(null);
                        if (mOnLoadMoreListener != null) {
                            mOnLoadMoreListener.onLoadMore();
                        }
                    }
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_BASIC) {
            return new ViewHolder(mInflater.inflate(R.layout.list_payment, viewGroup, false));
        } else if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return new ProgressViewHolder(mInflater.inflate(R.layout.list_loading, viewGroup, false));
        } else {
            throw new IllegalStateException("Invalid type, this type ot items " + viewType + " can't be handled");
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (getItemViewType(position) == ITEM_VIEW_TYPE_BASIC) {
            ((ViewHolder) holder).getBinding().setPayment(mItems.get(position));
            ((ViewHolder) holder).getBinding().executePendingBindings();
            if (mListener != null) {
                holder.itemView.setOnLongClickListener(v -> mListener.onItemLongClicked(v, position));
            }
        } else {
            ((ProgressViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return mItems != null ? mItems.size() : 0;
    }

    @Override
    public int getItemViewType(int position) {
        return mItems.get(position) != null ? ITEM_VIEW_TYPE_BASIC : ITEM_VIEW_TYPE_FOOTER;
    }

    /* ------------------------------------------------------------------------------------------ */
    /* ------------------------------------------------------------------------------------------ */
    public void resetItems(@NonNull List<PaymentModel> payments) {
        mLoading = true;
        mFirstVisibleItem = 0;
        mVisibleItemCount = 0;
        mTotalItemCount = 0;
        mPreviousTotal = 0;

        mItems.clear();
        addItems(payments);
    }

    public void addItems(@NonNull List<PaymentModel> newPayments) {
        for (PaymentModel p : newPayments) {
            addItem(p, false);
        }
        notifyDataSetChanged();
    }

    public void addItem(PaymentModel item) {
        addItem(item, true);
    }

    public void addItem(PaymentModel item, boolean shouldNotifyChange) {
        if (!mItems.contains(item)) {
            mItems.add(item);
            if (shouldNotifyChange) {
                notifyItemInserted(mItems.size() - 1);
            }
        }
    }

    public void removeItem(PaymentModel item) {
        int indexOfItem = mItems.indexOf(item);
        while (indexOfItem != -1) {
            this.mItems.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
            indexOfItem = mItems.indexOf(item);
        }
    }

    /**
     * ClickListener
     */
    public interface OnRecyclerListener {
        public boolean onItemLongClicked(View v, int position);
    }

    public interface OnLoadMoreListener {
        public void onLoadMore();
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * ViewHolders
     */
    /* ------------------------------------------------------------------------------------------ */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private ListPaymentBinding mBinding;

        public ViewHolder(View v) {
            super(v);
//            mBinding = ListPaymentBinding.inflate(mInflater);
            mBinding = ListPaymentBinding.bind(v);
        }

        public ListPaymentBinding getBinding() {
            return mBinding;
        }
    }

    public static class ProgressViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.progress_bar)
        public ProgressBar progressBar;

        public ProgressViewHolder(View v) {
            super(v);
            ButterKnife.bind(this, v);
        }
    }
}
