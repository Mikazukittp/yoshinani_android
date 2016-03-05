package app.android.ttp.mikazuki.yoshinani.event;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * @author haijimakazuki
 */
public class FetchListDataEvent<T> extends BaseEvent {

    private List<T> mData;


    public FetchListDataEvent(@NonNull final List<T> data) {
        this.mData = data;
    }

    @NonNull
    public List<T> getListData() {
        return mData;
    }

    public boolean isType(@NonNull final Class clazz) {
        return mData != null && !mData.isEmpty() && clazz.isInstance(mData.get(0));
    }

}
