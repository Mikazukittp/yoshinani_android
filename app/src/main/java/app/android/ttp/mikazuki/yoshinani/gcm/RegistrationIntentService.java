package app.android.ttp.mikazuki.yoshinani.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;
import java.util.Objects;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil;
import app.android.ttp.mikazuki.yoshinani.services.UserService;

/**
 * @author haijimakazuki
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = MyGcmListenerService.class.getName();
    private static final String[] TOPICS = {"global"};
    private UserService mUserService;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);
            subscribeTopics(token);

//            sharedPreferences.edit().putBoolean("SENT_TOKEN_TO_SERVER", true).apply();
        } catch (Exception e) {
            Log.e(TAG, "Failed to complete token refresh", e);
//            sharedPreferences.edit().putBoolean("SENT_TOKEN_TO_SERVER", false).apply();
        }
//        Intent registrationComplete = new Intent("REGISTRATION_COMPLETE");
//        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        final String oldToken = PreferenceUtil.getNotificationToken(this);
        mUserService = new UserService(this);
        if (oldToken != null && Objects.equals(token, oldToken)) {
            return;
        }

        ((oldToken != null) ? mUserService.updateToken(token, oldToken) : mUserService.registerToken(token))
                .subscribe(response -> {
                    if (response.isSuccess()) {
                        PreferenceUtil.putNotificationToken(this, token);
                    }
                });
    }

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }

}
