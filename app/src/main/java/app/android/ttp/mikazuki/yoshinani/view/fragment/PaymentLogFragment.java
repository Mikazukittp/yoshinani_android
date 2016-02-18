package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.parceler.Parcels;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchListDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.ShowDialogEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.PaymentModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.PaymentListAdapter;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.PaymentDetailDialogFragment;
import app.android.ttp.mikazuki.yoshinani.viewModel.PaymentViewModel;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class PaymentLogFragment extends Fragment {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.list_view)
    ListView mListView;
    private PaymentViewModel mPaymentViewModel;
    private GroupModel mGroupModel;
    private List<PaymentModel> mPayments;

    public PaymentLogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_log, container, false);
        ButterKnife.bind(this, view);

        mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));
        mPaymentViewModel = new PaymentViewModel(getActivity().getApplicationContext());
        mPaymentViewModel.getAll(mGroupModel.getId());

        mSwipeRefresh.setColorSchemeResources(R.color.theme600, R.color.accent500);
        mSwipeRefresh.setOnRefreshListener(() -> mPaymentViewModel.getAll(mGroupModel.getId()));
        ViewCompat.setNestedScrollingEnabled(mListView, true);
        mListView.setOnItemLongClickListener((parent, v, position, id) -> {
            PaymentModel payment = (PaymentModel) parent.getAdapter().getItem(position);
            ShowDialogEvent event = new ShowDialogEvent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("payment", Parcels.wrap(payment));
            event.setBundle(bundle);
            EventBus.getDefault().post(event);
            return true;
        });

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

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    public void onEvent(FetchListDataEvent<PaymentModel> event) {
        if (!event.isType(PaymentModel.class)) {
            return;
        }
        if (event.getListData() != null) {
            mPayments = event.getListData();
            mListView.setAdapter(new PaymentListAdapter(getActivity().getApplicationContext(), mPayments));
        }
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    public void onEvent(ShowDialogEvent event) {
        FragmentManager manager = getFragmentManager();
        PaymentDetailDialogFragment dialog = new PaymentDetailDialogFragment();
        dialog.setBundle(event.getBundle());
        dialog.show(manager, "dialog");
    }

}
