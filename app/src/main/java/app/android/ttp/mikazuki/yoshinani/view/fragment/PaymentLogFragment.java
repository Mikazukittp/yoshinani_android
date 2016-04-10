package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;
import java.util.Objects;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.event.ShowDialogEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.services.PaymentService;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.view.DividerItemDecoration;
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.PaymentListAdapter;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.PaymentDetailDialogFragment;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class PaymentLogFragment extends Fragment {

    private static final int VISIBLE_THRESHOLD = 20;

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private PaymentService mPaymentService;
    private GroupModel mGroupModel;
    private List<PaymentModel> mPayments;
    private PaymentListAdapter mRecyclerViewAdapter;

    public PaymentLogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_log, container, false);
        ButterKnife.bind(this, view);

        mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));
        mPaymentService = new PaymentService(getActivity().getApplicationContext());

        mSwipeRefresh.setColorSchemeResources(R.color.theme600, R.color.accent600);
        mSwipeRefresh.setOnRefreshListener(() -> refresh());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity().getApplicationContext()));

        refresh();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void refresh() {
        mSwipeRefresh.setRefreshing(true);
        mPaymentService.getAll(mGroupModel.getId());
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(FetchListDataEvent<PaymentModel> event) {
        if (Objects.equals(event.getTag(), "first")) {
            mPayments = event.getListData();
            mRecyclerViewAdapter = new PaymentListAdapter(mRecyclerView, mPayments,
                    (v, position) -> {
                        ShowDialogEvent dialogEvent = new ShowDialogEvent();
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("payment", Parcels.wrap(mPayments.get(position)));
                        dialogEvent.setBundle(bundle);
                        EventBus.getDefault().post(dialogEvent);
                        return true;
                    },
                    () -> mPaymentService.getNext(mGroupModel.getId(), mPayments.get(mPayments.size() - 2).getId())
            );
            mRecyclerView.setAdapter(mRecyclerViewAdapter);
        } else if (Objects.equals(event.getTag(), "next")) {
            mRecyclerViewAdapter.addItems(event.getListData());
            mRecyclerViewAdapter.removeItem(null);
        }
//        }
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Subscribe
    public void onEvent(ShowDialogEvent event) {
        FragmentManager manager = getFragmentManager();
        PaymentDetailDialogFragment dialog = new PaymentDetailDialogFragment();
        dialog.setArguments(event.getBundle());
        dialog.show(manager, "dialog");
    }

    @Subscribe
    public void onEvent(RefreshEvent event) {
        refresh();
    }

}
