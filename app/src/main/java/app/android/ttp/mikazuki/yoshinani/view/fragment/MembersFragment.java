package app.android.ttp.mikazuki.yoshinani.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.MemberListAdapter;
import app.android.ttp.mikazuki.yoshinani.services.GroupService;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MembersFragment extends Fragment {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.list_view)
    ListView mListView;

    private List<UserModel> mUserModels;
    private GroupService mGroupService;
    private GroupModel mGroupModel;

    public MembersFragment() {
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * lifecycle methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        ButterKnife.bind(this, view);

        mGroupModel = Parcels.unwrap(getArguments().getParcelable(Constants.BUNDLE_GROUP_KEY));
        mGroupService = new GroupService(getActivity().getApplicationContext());
        mGroupService.get(mGroupModel.getId());

        mSwipeRefresh.setColorSchemeResources(R.color.theme600, R.color.accent500);
        mSwipeRefresh.setOnRefreshListener(() -> mGroupService.get(mGroupModel.getId()));
        ViewCompat.setNestedScrollingEnabled(mListView, true);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
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
    @Subscribe
    public void onEvent(FetchDataEvent<GroupModel> event) {
        if (!event.isType(GroupModel.class)) {
            return;
        }
        if (event.getData() != null) {
            GroupModel group = event.getData();
            mUserModels = group.getMembers();
            mUserModels.addAll(group.getInvitedMembers());
            mListView.setAdapter(new MemberListAdapter(getActivity().getApplicationContext(), mUserModels, mGroupModel));
        }
        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

}
