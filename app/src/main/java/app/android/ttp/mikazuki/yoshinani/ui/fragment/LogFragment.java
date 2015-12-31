package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitPaymentRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.PaymentRepository;
import app.android.ttp.mikazuki.yoshinani.ui.activity.PostActivity;
import app.android.ttp.mikazuki.yoshinani.ui.adapter.PaymentListAdapter;
import app.android.ttp.mikazuki.yoshinani.ui.event.ActivityTransitionEvent;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * A placeholder fragment containing a simple view.
 */
public class LogFragment extends MainFragment {

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.listview)
    ListView mListView;

    private PaymentRepository mPaymentRepository;

    public static LogFragment newInstance() {
        LogFragment fragment = new LogFragment();
        return fragment;
    }

    public LogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        ButterKnife.bind(this, view);

        mPaymentRepository = new RetrofitPaymentRepository(getActivity().getApplicationContext());

        mSwipeRefresh.setColorSchemeResources(R.color.theme600, R.color.accent500);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setListData(1);
            }
        });
        setListData(1);

        return view;
    }

    private void setListData(int groupId) {
        mPaymentRepository.getAll(groupId, new BaseCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                PaymentListAdapter adapter = new PaymentListAdapter(getActivity().getApplicationContext(), 0, payments);
                mListView.setAdapter(adapter);
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
                if (mSwipeRefresh.isRefreshing()) {
                    mSwipeRefresh.setRefreshing(false);
                }
            }
        });
    }

    @OnClick(R.id.fab)
    public void onButtonPressed(View v) {
        EventBus.getDefault().post(new ActivityTransitionEvent(PostActivity.class));
//        Snackbar.make(mCoordinatorLayout, getString(R.string.btn_clicked), Snackbar.LENGTH_LONG)
//                .setAction(getString(R.string.goToMain), (View v) -> { mListener.createNewPayment(); })
//                .show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
