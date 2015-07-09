package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitGroupRepository;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitPaymentRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.ui.adapter.PaymentListAdapter;
import app.android.ttp.mikazuki.yoshinani.ui.adapter.UserListAdapter;
import app.android.ttp.mikazuki.yoshinani.ui.listener.ToolBarListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubFragment extends Fragment {

    @Bind(R.id.tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.overViewList)
    ListView overViewList;

    private OnFragmentInteractionListener mListener;
    private ToolBarListener mToolbarListener;
    private RetrofitGroupRepository mGroupRepository;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sub, container, false);
        ButterKnife.bind(this, view);

        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mToolbarListener.onMenuClicked();
            }
        });

        mGroupRepository = new RetrofitGroupRepository(getActivity().getApplicationContext());

        setOverView();

        return view;
    }

    public void setOverView() {
        mGroupRepository.getOverView(new BaseCallback<List<User>>() {

            @Override
            public void onSuccess(List<User> users) {
                UserListAdapter adapter = new UserListAdapter(getActivity().getApplicationContext(), 0, users);
                for (User user: users) {
                    Log.e("!!!!!", user.getName());
                }

                overViewList.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
            }
        });
    }

    @OnClick(R.id.fab)
    public void onButtonPressed(View v) {
        Snackbar.make(mCoordinatorLayout, getString(R.string.btn_clicked), Snackbar.LENGTH_LONG)
                .setAction(getString(R.string.goToMain), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.goToMainView();
                    }
                })
                .show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
            mToolbarListener = (ToolBarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mToolbarListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnFragmentInteractionListener {
        public void goToMainView();
    }

}
