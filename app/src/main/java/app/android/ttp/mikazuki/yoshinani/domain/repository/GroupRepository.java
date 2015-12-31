package app.android.ttp.mikazuki.yoshinani.domain.repository;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Group;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Payment;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface GroupRepository extends BaseRepository<Payment> {

    public void getOverView(int gourpId, BaseCallback<Group> cb);

    public void getAll(int userId, BaseCallback<List<Group>> cb);
}
