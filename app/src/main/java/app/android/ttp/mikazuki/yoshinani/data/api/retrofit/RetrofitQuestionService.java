package app.android.ttp.mikazuki.yoshinani.data.api.retrofit;

import android.content.Context;

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
    public void getAllQuestions(Context context,Callback<List<Question>> cb);

}
