package app.android.ttp.mikazuki.yoshinani.gcm

import android.content.Intent

import com.google.android.gms.iid.InstanceIDListenerService

/**
 * @author haijimakazuki
 */
class MyInstanceIDListenerService : InstanceIDListenerService() {

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    override fun onTokenRefresh() {
        val intent = Intent(this, RegistrationIntentService::class.java)
        startService(intent)
    }
}
