package app.android.ttp.mikazuki.yoshinani.event;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by haijimakazuki on 15/09/11.
 */
public class ActivityTransitionEvent extends BaseEvent {
    private final Class<? extends Activity> mDest;
    private Bundle mBundle;
    private boolean addToBackStack = true;

    public ActivityTransitionEvent(final Class<? extends Activity> dest) {
        this(dest, true);
    }

    public ActivityTransitionEvent(final Class<? extends Activity> dest,
                                   final boolean addToBackStack) {
        this.mDest = dest;
        this.addToBackStack = addToBackStack;
    }

    public Class<? extends Activity> getDestinationActivity() {
        return mDest;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public void setBundle(Bundle bundle) {
        this.mBundle = bundle;
    }

    public boolean doesAddToBackStack() {
        return addToBackStack;
    }

    public void setAddToBackStack(boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
    }
}

