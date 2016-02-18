package app.android.ttp.mikazuki.yoshinani.event;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by haijimakazuki on 16/01/17.
 */
public class FragmentTransitionEvent {
    private final Fragment mDest;
    private Bundle mBundle;
    private boolean addToBackStack = true;

    public FragmentTransitionEvent(final Fragment dest) {
        mDest = dest;
    }

    public Fragment getDestinationFragment() {
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
