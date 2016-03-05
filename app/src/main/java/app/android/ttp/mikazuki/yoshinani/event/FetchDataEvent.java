package app.android.ttp.mikazuki.yoshinani.event;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * @author haijimakazuki
 */
public class FetchDataEvent<T> extends BaseEvent {

    private T mData;

    public FetchDataEvent(@Nullable final T data) {
        this.mData = data;
    }

    public T getData() {
        return mData;
    }

    public boolean isType(@NonNull final Class clazz) {
        return mData != null && clazz.isInstance(mData);
    }
}
