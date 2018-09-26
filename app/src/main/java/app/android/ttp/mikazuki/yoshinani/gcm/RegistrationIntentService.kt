package app.android.ttp.mikazuki.yoshinani.gcm

import android.app.IntentService
import android.content.Intent
import android.util.Log
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.repository.preference.PreferenceUtil
import app.android.ttp.mikazuki.yoshinani.services.UserService
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID

/**
 * @author haijimakazuki
 */
class RegistrationIntentService : IntentService(TAG) {
    private var mUserService: UserService? = null

    override fun onHandleIntent(intent: Intent?) {
        try {
            val instanceID = InstanceID.getInstance(this)
            val token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)
            Log.i(TAG, "GCM Registration Token: $token")

            sendRegistrationToServer(token)
            //            subscribeTopics(token);
        } catch (e: Exception) {
            Log.e(TAG, "Failed to complete token refresh", e)
        }

    }

    private fun sendRegistrationToServer(token: String) {
        val oldToken = PreferenceUtil.getNotificationToken(this)
        mUserService = UserService(this)
        if (oldToken != null && token == oldToken) {
            return
        }

        (if (oldToken != null) mUserService!!.updateToken(token, oldToken) else mUserService!!.registerToken(token))
                .subscribe { response ->
                    if (response.isSuccess) {
                        PreferenceUtil.putNotificationToken(this, token)
                    }
                }
    }

    companion object {

        private val TAG = MyGcmListenerService::class.java!!.getName()
        private val TOPICS = arrayOf("global")
    }

    //    private void subscribeTopics(String token) throws IOException {
    //        GcmPubSub pubSub = GcmPubSub.getInstance(this);
    //        for (String topic : TOPICS) {
    //            pubSub.subscribe(token, "/topics/" + topic, null);
    //        }
    //    }

}
