package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
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
import app.android.ttp.mikazuki.yoshinani.ui.adapter.PaymentListAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A placeholder fragment containing a simple view.
 */
public class LogFragment extends MainFragment {

    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.listview)
    ListView mListView;

    private PaymentRepository mPaymentRepository;
    private OnFragmentInteractionListener mListener;

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

        setListData();

        return view;
    }

    private void setListData() {
        mPaymentRepository.getAll(new BaseCallback<List<Payment>>() {
            @Override
            public void onSuccess(List<Payment> payments) {
                PaymentListAdapter adapter = new PaymentListAdapter(getActivity().getApplicationContext(), 0, payments);
                mListView.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
            }
        });
    }

    @OnClick(R.id.fab)
    public void onButtonPressed(View v) {
        mListener.createNewPayment();
//        Snackbar.make(mCoordinatorLayout, getString(R.string.btn_clicked), Snackbar.LENGTH_LONG)
//                .setAction(getString(R.string.goToMain), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        mListener.createNewPayment();
//                    }
//                })
//                .show();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnFragmentInteractionListener {
        public void createNewPayment();
    }

}
