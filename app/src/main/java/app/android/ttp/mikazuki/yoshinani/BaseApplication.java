package app.android.ttp.mikazuki.yoshinani;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class BaseApplication extends MultiDexApplication {
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     *
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//            analytics.setLocalDispatchPeriod(15);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
//            mTracker.enableAutoActivityTracking(true);
//            mTracker.enableExceptionReporting(true);
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
//        analytics.setLocalDispatchPeriod(15);
        if (BuildConfig.DEBUG) {
            analytics.setDryRun(true);
            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        }
        mTracker = analytics.newTracker(R.xml.global_tracker);
//        mTracker.enableAutoActivityTracking(true);
//        mTracker.enableExceptionReporting(true);
    }
}