package com.szantog.brew_e.clients.brewe;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.szantog.brew_e.R;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.szantog.brew_e.ui.MainController.CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;
    private static int NOTIFICATION_ID = 1;

    @Override
    public void onCreate() {
        super.onCreate();
        builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.coffee_beans_32x32);
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null) {
            builder.setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            notificationManager.notify(++NOTIFICATION_ID, builder.build());
        }
    }
}
