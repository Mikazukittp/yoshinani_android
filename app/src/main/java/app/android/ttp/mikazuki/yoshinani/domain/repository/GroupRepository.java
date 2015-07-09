package app.android.ttp.mikazuki.yoshinani.domain.repository;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;
import app.android.ttp.mikazuki.yoshinani.domain.entity.User;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface GroupRepository extends BaseRepository<Payment> {
    public void getOverView(BaseCallback<Group> cb);
}
