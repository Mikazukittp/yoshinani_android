package app.android.ttp.mikazuki.yoshinani.domain.repository;

/**
 * Created by haijimakazuki on 15/07/07.
 */
public interface BaseCallback<T> {

    public void onSuccess(T t);

    public void onFailure();

}
