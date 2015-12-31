package app.android.ttp.mikazuki.yoshinani.domain.repository;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.User;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public interface UserRepository extends BaseRepository<User> {

    void getAll(int groupId, BaseCallback<List<User>> cb);

    void signIn(String email, String password, BaseCallback<User> cb);
}
