package app.android.ttp.mikazuki.yoshinani.event;

import android.os.Bundle;

/**
 * Created by haijimakazuki on 16/01/02.
 */
public class ShowDialogEvent {

    private String mTitle;

    private Bundle mBundle;

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }

}
