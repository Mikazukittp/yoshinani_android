package app.android.ttp.mikazuki.yoshinani.event;

import android.support.annotation.NonNull;

/**
 * Created by haijimakazuki on 16/01/02.
 */
public class ShowSnackbarEvent extends BaseEvent {

    private String mMessage;


    public ShowSnackbarEvent(@NonNull final String message) {
        this.mMessage = message;
    }

    public String getmMessage() {
        return mMessage;
    }


}
