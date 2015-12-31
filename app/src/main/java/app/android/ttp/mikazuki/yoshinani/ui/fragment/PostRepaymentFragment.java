package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.android.ttp.mikazuki.yoshinani.R;
import butterknife.ButterKnife;

public class PostRepaymentFragment extends PostFragment {

    public static PostRepaymentFragment newInstance() {
        PostRepaymentFragment fragment = new PostRepaymentFragment();
        return fragment;
    }

    public PostRepaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_repayment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
