package com.app.tubemarket.notifications;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.app.tubemarket.R;
import com.app.tubemarket.broadcast_notification.NotificationBroadCastReceiver;
import com.app.tubemarket.models.UserModel;
import com.app.tubemarket.preferences.Preferences;
import com.app.tubemarket.tags.Tags;
import com.app.tubemarket.uis.activity_home.HomeActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;


import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class FireBaseMessaging extends FirebaseMessagingService {

    private Preferences preferences = Preferences.getInstance();
    private Map<String, String> map;
    private final String GROUP_KEY = "MyAppNotGroup1929Key";


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }

        if (getSession().equals(Tags.session_login)) {

            String notification_type = map.get("notification_type");
            manageNotification(map);

        }
    }

    private void manageNotification(Map<String, String> map) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNewNotificationVersion(map);
        } else {
            createOldNotificationVersion(map);

        }

    }


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);


    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createNewNotificationVersion(Map<String, String> map) {
        String title = map.get("title");
        String body = map.get("message");
        Intent intent;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);

        Intent cancelIntent = new Intent(this, NotificationBroadCastReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, cancelIntent, 0);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);


        String notification_type = map.get("noti_type");
        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        String CHANNEL_ID = "my_channel_02";
        CharSequence CHANNEL_NAME = "my_channel_name";
        int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;

        final NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE);

        channel.setShowBadge(true);
        channel.setSound(Uri.parse(sound_Path), new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION_EVENT)
                .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
                .build()
        );



        intent = new Intent(this, HomeActivity.class);
        builder.setContentTitle(title);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);


        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        builder.setChannelId(CHANNEL_ID);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);
        builder.setAutoCancel(true);
        builder.setGroupSummary(true);
        builder.setGroup(GROUP_KEY);
        builder.setDeleteIntent(cancelPendingIntent);


        builder.setLargeIcon(bitmap);
        if (manager != null) {

            manager.createNotificationChannel(channel);
            manager.notify(Tags.not_tag, Tags.not_id, builder.build());

        }

    }

    private void createOldNotificationVersion(Map<String, String> map) {

        String notification_type = map.get("noti_type");
        String sound_Path = "";
        if (sound_Path.isEmpty()) {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            sound_Path = uri.toString();
        }

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);
        builder.setSmallIcon(R.mipmap.ic_launcher_round);


        String title = map.get("title");
        String body = map.get("message");
        Intent intent;

        intent = new Intent(this, HomeActivity.class);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
        taskStackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent = taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);
        Intent cancelIntent = new Intent(this, NotificationBroadCastReceiver.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, cancelIntent, 0);


        builder.setContentTitle(title);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(body));
        builder.setContentIntent(pendingIntent);
        builder.setDeleteIntent(cancelPendingIntent);
        builder.setLargeIcon(bitmap);
        builder.setGroupSummary(true);
        builder.setGroup(GROUP_KEY);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {

            manager.notify(Tags.not_tag, Tags.not_id, builder.build());

        }


    }


    private UserModel getUserData() {
        return preferences.getUserData(this);
    }

    private String getSession() {
        return preferences.getSession(this);
    }


}
