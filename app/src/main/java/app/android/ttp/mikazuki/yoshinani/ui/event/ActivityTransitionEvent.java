package app.android.ttp.mikazuki.yoshinani.ui.event;

/**
 * Created by haijimakazuki on 15/09/11.
 */
public class ActivityTransitionEvent {
    private final Class mDest;

    public ActivityTransitionEvent(final Class dest) {
        mDest = dest;
    }

    public Class getDestinationActivity() {
        return mDest;
    }
}

