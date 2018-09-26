package app.android.ttp.mikazuki.yoshinani

import android.app.Application
import android.support.multidex.MultiDexApplication

import com.facebook.stetho.Stetho
import com.google.android.gms.analytics.GoogleAnalytics
import com.google.android.gms.analytics.Tracker
import com.squareup.leakcanary.LeakCanary

import jp.line.android.sdk.LineSdkContextManager

/**
 * This is a subclass of [Application] used to provide shared objects for this app, such as
 * the [Tracker].
 */
class BaseApplication : MultiDexApplication() {
    private var mTracker: Tracker? = null

    /**
     * Gets the default [Tracker] for this [Application].
     *
     * @return tracker
     */
    val defaultTracker: Tracker?
        @Synchronized get() {
            if (mTracker == null) {
                createTracker()
            }
            return mTracker
        }

    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)

        createTracker()

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build())

        LineSdkContextManager.initialize(this)
    }

    private fun createTracker() {
        val analytics = GoogleAnalytics.getInstance(this)
        analytics.setLocalDispatchPeriod(0)
        //        if (BuildConfig.DEBUG) {
        //            analytics.setDryRun(true);
        //            analytics.getLogger().setLogLevel(Logger.LogLevel.VERBOSE);
        //        }
        // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
        mTracker = analytics.newTracker(R.xml.app_tracker)
        mTracker!!.enableExceptionReporting(true)
    }

}
