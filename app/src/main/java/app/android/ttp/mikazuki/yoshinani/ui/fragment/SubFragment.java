package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.ui.listener.ToolBarListener;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SubFragment extends Fragment {

    @Bind(R.id.tool_bar)
    Toolbar mToolbar;
    @Bind(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    private OnFragmentInteractionListener mListener;
    private ToolBarListener mToolbarListener;

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
        return view;
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
