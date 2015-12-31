package app.android.ttp.mikazuki.yoshinani.ui.activity;

import android.os.Bundle;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository.RetrofitGroupRepository;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.GroupRepository;
import butterknife.ButterKnife;

public class GroupsActivity extends BaseActivity {

    List<Group> mGroups;
    private GroupRepository mUserRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);

//        getAppComponent().inject(this);
        mUserRepository = new RetrofitGroupRepository(this);

        mUserRepository.getAll(1, new BaseCallback<List<Group>>() {
            @Override
            public void onSuccess(List<Group> groups) {
                mGroups = groups;
            }

            @Override
            public void onFailure() {

            }
        });

        ButterKnife.bind(this);
    }


}
