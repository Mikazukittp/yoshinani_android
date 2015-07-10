package app.android.ttp.mikazuki.yoshinani.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitGroupRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.ui.adapter.UserListAdapter;
import butterknife.Bind;
import butterknife.ButterKnife;

public class OverviewFragment extends MainFragment {

    @Bind(R.id.overViewList)
    ListView overViewList;

    private RetrofitGroupRepository mGroupRepository;

    public static OverviewFragment newInstance() {
        OverviewFragment fragment = new OverviewFragment();
        return fragment;
    }

    public OverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_overview, container, false);
        ButterKnife.bind(this, view);

        mGroupRepository = new RetrofitGroupRepository(getActivity().getApplicationContext());

        setOverView();

        return view;
    }

    public void setOverView() {
        mGroupRepository.getOverView(new BaseCallback<Group>() {

            @Override
            public void onSuccess(Group group) {
                List<User> users = group.getMembers();
                UserListAdapter adapter = new UserListAdapter(getActivity().getApplicationContext(), 0, users);
                overViewList.setAdapter(adapter);
            }

            @Override
            public void onFailure() {
                Log.e("!!!!!", "failure");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
