package app.android.ttp.mikazuki.yoshinani;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import org.greenrobot.eventbus.EventBus;

import app.android.ttp.mikazuki.yoshinani.event.MaintenanceEvent;
import app.android.ttp.mikazuki.yoshinani.model.Status;

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
        createTracker();
        Firebase.setAndroidContext(this);
        Firebase statusRef = new Firebase(getString(R.string.firebase_status_url));
        statusRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Status status = dataSnapshot.getValue(Status.class);
                EventBus.getDefault().post(new MaintenanceEvent(status));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });
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