package app.android.ttp.mikazuki.yoshinani.data.repository.api.retrofit;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.domain.entity.Question;
import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public interface RetrofitQuestionService {

    static final String PATH_QUESTION = "/questions";

    @GET(PATH_QUESTION)
    public void getAllQuestions(Callback<List<Question>> cb);

}
