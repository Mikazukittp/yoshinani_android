package app.android.ttp.mikazuki.yoshinani.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GcmListenerService;

import org.parceler.Parcels;

import java.util.Objects;

import app.android.ttp.mikazuki.yoshinani.R;
import app.android.ttp.mikazuki.yoshinani.model.GroupModel;
import app.android.ttp.mikazuki.yoshinani.services.UserService;
import app.android.ttp.mikazuki.yoshinani.utils.Constants;
import app.android.ttp.mikazuki.yoshinani.view.activity.GroupActivity;
import app.android.ttp.mikazuki.yoshinani.view.activity.MainActivity;

/**
 * @author haijimakazuki
 */
public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";
    private UserService mUserService;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        final String message = data.getString("message");
        final String type = data.getString("type");
        if (Objects.equals(type, "new_payment")) {
            Intent intent = new Intent(this, GroupActivity.class);
            intent.putExtra(Constants.BUNDLE_GROUP_KEY, Parcels.wrap(new GroupModel(Integer.parseInt(data.getString("group_id")), null, null, null, null)));
            sendNotification("新しい精算の投稿", message, intent);
        } else if (Objects.equals(type, "invitation")) {
            Intent intent = new Intent(this, MainActivity.class);
            sendNotification("新グループへの招待", message, intent);
        }
    }

    private void sendNotification(@NonNull final String title,
                                  @NonNull final String message,
                                  @NonNull final Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.icon_for_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setVibrate(new long[]{0, 100, 100, 50, 50, 50})
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    class Payment {
        int paymentId;
        int groupId;
    }
}
