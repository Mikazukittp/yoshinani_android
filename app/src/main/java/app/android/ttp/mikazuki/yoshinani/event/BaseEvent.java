package app.android.ttp.mikazuki.yoshinani.event;

import android.support.annotation.Nullable;

/**
 * @author haijimakazuki
 */
public class BaseEvent {

    private String mTag;

    @Nullable
    public String getTag() {
        return mTag;
    }

    public BaseEvent setTag(@Nullable final String mTag) {
        this.mTag = mTag;
        return this;
    }

}
