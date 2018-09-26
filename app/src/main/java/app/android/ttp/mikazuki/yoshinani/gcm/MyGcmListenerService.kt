package app.android.ttp.mikazuki.yoshinani.gcm

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import app.android.ttp.mikazuki.yoshinani.R
import app.android.ttp.mikazuki.yoshinani.model.GroupModel
import app.android.ttp.mikazuki.yoshinani.services.UserService
import app.android.ttp.mikazuki.yoshinani.utils.Constants
import app.android.ttp.mikazuki.yoshinani.view.activity.GroupActivity
import app.android.ttp.mikazuki.yoshinani.view.activity.MainActivity
import com.google.android.gms.gcm.GcmListenerService
import org.parceler.Parcels

/**
 * @author haijimakazuki
 */
class MyGcmListenerService : GcmListenerService() {
    private val mUserService: UserService? = null

    override fun onMessageReceived(from: String?, data: Bundle?) {
        val message = data!!.getString("message")
        val type = data.getString("type")
        if (type == "new_payment") {
            val intent = Intent(this, GroupActivity::class.java)
            intent.putExtra(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(GroupModel(Integer.parseInt(data.getString("group_id")!!), null, null, null, null)))
            sendNotification("新しい精算の投稿", message!!, intent)
        } else if (type == "invitation") {
            val intent = Intent(this, MainActivity::class.java)
            sendNotification("新グループへの招待", message!!, intent)
        }
    }

    private fun sendNotification(title: String,
                                 message: String,
                                 intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_for_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(longArrayOf(0, 100, 100, 50, 50, 50))
                .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    internal inner class Payment {
        var paymentId: Int = 0
        var groupId: Int = 0
    }

    companion object {

        private val TAG = "MyGcmListenerService"
    }
}
