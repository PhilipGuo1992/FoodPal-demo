package com.example.phili.foodpaldemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Objects;

/**
 * author: zongming
 * Ref - https://firebase.google.com/docs/cloud-messaging/android/topic-messaging
 */

public class FoodPalService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String title;
        String body;
        String uId;
        Log.i("Receiving stuf: ", " get invoked");

        if (remoteMessage.getData().size() > 0) {
            title = remoteMessage.getData().get("title");
            body = remoteMessage.getData().get("body");
            uId = remoteMessage.getData().get("newName");
            pushNotifications(title, body, uId);
        }

    }

    public void pushNotifications(String title, String body, String uId) {
        String mUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        if (!mUid.equals(uId)) {
            // Ref - https://developer.android.com/training/notify-user/build-notification.html
            Intent intent = new Intent(getApplicationContext(), MainHomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    getApplicationContext(), "0")
                    .setSmallIcon(R.drawable.cast_ic_notification_small_icon)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText("Much longer text that cannot fit one line..."))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setDefaults(NotificationCompat.DEFAULT_ALL)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

            // notificationId is a unique int for each notification that you must define
            notificationManager.notify(0, mBuilder.build());
        }
    }


}
