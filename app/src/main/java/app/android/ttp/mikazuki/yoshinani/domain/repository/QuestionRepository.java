package app.android.ttp.mikazuki.yoshinani.domain.repository;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Question;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public interface QuestionRepository extends BaseRepository<Question> {
    public void getAll(BaseCallback<List<Question>> cb);
}
