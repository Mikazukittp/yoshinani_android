package app.android.ttp.mikazuki.yoshinani.domain.repository;

import java.util.HashMap;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Question;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Token;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public interface AuthRepository extends BaseRepository {
    public void signIn(String email, String password, BaseCallback<Token> cb);
}
