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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;
import java.util.Map;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.event.ActivityTransitionEvent;
import app.android.ttp.mikazuki.yoshinani.event.FetchDataEvent;
import app.android.ttp.mikazuki.yoshinani.event.RefreshEvent;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.model.TotalModel;
import app.android.ttp.mikazuki.yoshinani.model.UserModel;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.utils.TextUtils;
import app.android.ttp.mikazuki.yoshinani.view.activity.GroupActivity;
import app.android.ttp.mikazuki.yoshinani.view.adapter.list.GroupListAdapter;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.AcceptDialogFragment;
import app.android.ttp.mikazuki.yoshinani.view.fragment.dialog.GroupDetailDialogFragment;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;

/**
 * @author haijimakazuki
 */
public class MainFragment extends Fragment {

    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @Bind(R.id.list_view)
    ListView mListView;
    @Bind(R.id.total_amount)
    TextView mTotalAmount;
    @Bind(R.id.adView)
    AdView mAdView;

    private List<GroupModel> mGroups;
    private UserService mUserService;
    private UserModel me;
    private List<GroupModel> mInvitedGroups;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);

        // ListViewの設定
        mSwipeRefresh.setColorSchemeResources(R.color.theme600, R.color.accent500);
        mSwipeRefresh.setOnRefreshListener(() -> EventBus.getDefault().post(new RefreshEvent(true)));
        ViewCompat.setNestedScrollingEnabled(mListView, true);
        final View footer = getActivity().getLayoutInflater().inflate(R.layout.list_footer_group, null, false);
        footer.setOnClickListener(v -> new GroupDetailDialogFragment().show(getActivity().getSupportFragmentManager(), "createGroup"));
        mListView.addFooterView(footer);
        mListView.setOnItemClickListener((parent, v, position, id) -> {
            final Bundle bundle = new Bundle();
            bundle.putParcelable(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(mGroups.get(position)));
            if (position < me.getGroups().size()) {
                ActivityTransitionEvent event = new ActivityTransitionEvent(GroupActivity.class);
                event.setBundle(bundle);
                EventBus.getDefault().post(event);
            } else {
                AcceptDialogFragment dialog = new AcceptDialogFragment();
                dialog.setArguments(bundle);
                dialog.show(getActivity().getSupportFragmentManager(), "notification");
            }
        });

        // ViewModelの宣言
        mUserService = new UserService(getActivity().getApplicationContext());

        // データの取得
        refresh();

        // Admobの配信
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .addTestDevice(getString(R.string.test_device_id))
                .build();
        mAdView.loadAd(adRequest);
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
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getString(R.string.app_name));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void refresh() {
        mSwipeRefresh.setRefreshing(true);
        mUserService.getMe();
    }

    /* ------------------------------------------------------------------------------------------ */
    /*
     * onEvent methods
     */
    /* ------------------------------------------------------------------------------------------ */
    @Subscribe
    public void onEvent(FetchDataEvent<UserModel> event) {
        if (!event.isType(UserModel.class)) {
            return;
        }
        if (event.getData() == null) {
            return;
        }

        me = event.getData();
        mGroups = Lists.newArrayList(me.getGroups());
        mGroups.addAll(me.getInvitedGroups());
        // 毎回アクセスするためHashに持つ
        Map<Integer, TotalModel> totals = Maps.newHashMap();
        for (TotalModel total : me.getTotals()) {
            totals.put(total.getGroupId(), total);
        }
        mListView.setAdapter(new GroupListAdapter(
                getActivity().getApplication(),
                GroupListAdapter.createGroupListItems(me.getGroups(), me.getInvitedGroups(), totals)
        ));

        // 各グループでの支払額の合計を算出
        final int totalAmount = Observable.from(mGroups)
                .map(GroupModel::getId)
                .map(totals::get)
                .filter(total -> total != null)
                .map(TotalModel::getResult)
                .defaultIfEmpty(0)
                .reduce((sum, v) -> sum + v)
                .toBlocking().single();
        mTotalAmount.setText(TextUtils.wrapCurrency(totalAmount));

        if (mSwipeRefresh.isRefreshing()) {
            mSwipeRefresh.setRefreshing(false);
        }
    }

    @Subscribe
    public void onEvent(RefreshEvent event) {
        refresh();
    }
}
