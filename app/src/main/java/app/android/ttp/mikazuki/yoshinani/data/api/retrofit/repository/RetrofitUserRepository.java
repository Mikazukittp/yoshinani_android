package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.User;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.UserRepository;

/**
 * Created by haijimakazuki on 15/07/09.
 */
public class RetrofitUserRepository implements UserRepository {

    @Override
    public void get(int id, BaseCallback<User> cb) {

    }

    @Override
    public void getAll(BaseCallback<List<User>> cb) {

    }

    @Override
    public void create(User user, BaseCallback<User> cb) {

    }

    @Override
    public void update(User user, BaseCallback<User> cb) {

    }

    @Override
    public void delete(int id, BaseCallback<User> cb) {

    }
}
