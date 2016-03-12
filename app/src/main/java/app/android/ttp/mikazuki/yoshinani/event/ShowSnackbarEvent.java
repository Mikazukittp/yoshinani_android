package app.android.ttp.mikazuki.yoshinani.event;

import android.support.annotation.NonNull;

/**
 * @author haijimakazuki
 */
public class ShowSnackbarEvent extends BaseEvent {

    private String mMessage;


    public ShowSnackbarEvent(@NonNull final String message) {
        this.mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }


}
