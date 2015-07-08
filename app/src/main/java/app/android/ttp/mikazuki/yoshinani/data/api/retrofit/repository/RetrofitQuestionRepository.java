package app.android.ttp.mikazuki.yoshinani.data.api.retrofit.repository;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import app.android.ttp.mikazuki.yoshinani.data.api.ApiUtil;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.RetrofitQuestionService;
import app.android.ttp.mikazuki.yoshinani.data.api.retrofit.interceptor.QuestionRequestInterceptor;
import app.android.ttp.mikazuki.yoshinani.domain.entity.Question;
import app.android.ttp.mikazuki.yoshinani.domain.repository.BaseCallback;
import app.android.ttp.mikazuki.yoshinani.domain.repository.QuestionRepository;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public class RetrofitQuestionRepository implements QuestionRepository {

    final private String TAG = "QuestionRepository";

    Gson GSON = new GsonBuilder().create();

    RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(ApiUtil.API_URL)
            .setConverter(new GsonConverter(GSON))
            .setRequestInterceptor(new QuestionRequestInterceptor())
            .build();

    final RetrofitQuestionService API = REST_ADAPTER.create(RetrofitQuestionService.class);

    @Override
    public void get(int id, BaseCallback<Question> cb) {

    }

    @Override
    public void getAll(final BaseCallback<List<Question>> cb) {
        API.getAllQuestions(new Callback<List<Question>>() {
            @Override
            public void success(List<Question> questions, Response response) {
                cb.onSuccess(questions);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    Log.e(TAG, error.getResponse().getStatus() + " " + error.getMessage());
                } else {
                    Log.e(TAG, error.getMessage());
                }
                cb.onFailure();
            }
        });
    }

    @Override
    public void create(Question question, BaseCallback<Question> cb) {

    }

    @Override
    public void update(Question question, BaseCallback<Question> cb) {

    }

    @Override
    public void delete(int id, BaseCallback<Question> cb) {

    }
}
