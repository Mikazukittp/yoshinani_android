package app.android.ttp.mikazuki.yoshinani;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.facebook.stetho.Stetho;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.squareup.leakcanary.LeakCanary;

import jp.line.android.sdk.LineSdkContextManager;

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
            createTracker();
        }
        return mTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        createTracker();

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());

        LineSdkContextManager.initialize(this);
    }

    private void createTracker() {
        GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
        analytics.setLocalDispatchPeriod(0);
//        if (BuildConfig.DEBUG) {
//            analytics.setDryRun(true);
//            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
//        }
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        mTracker = analytics.newTracker(R.xml.app_tracker);
        mTracker.enableExceptionReporting(true);
    }

}
