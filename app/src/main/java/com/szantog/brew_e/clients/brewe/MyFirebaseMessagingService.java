package com.szantog.brew_e.clients.brewe;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.szantog.brew_e.R;
import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.common.SharedPreferencesHandler;
import com.szantog.brew_e.ui.MainController;
import com.szantog.brew_e.ui.MainViewModel;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.szantog.brew_e.ui.MainController.CHANNEL_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;
    private static int NOTIFICATION_ID = 1;

    private SharedPreferencesHandler sharedPreferencesHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesHandler = new SharedPreferencesHandler(this);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.coffee_beans_32x32);
        notificationManager = NotificationManagerCompat.from(this);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null){
            builder.setContentTitle(remoteMessage.getNotification().getTitle())
                    .setContentText(remoteMessage.getNotification().getBody());
            builder.setPriority(NotificationCompat.PRIORITY_HIGH);
            notificationManager.notify(++NOTIFICATION_ID, builder.build());
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FirebaseMssagingService", "Refreshed token: " + token);
        Call<Void> call = RetrofitClient.getInstance().uploadFirbaseToken("PHPSESSID=" + sharedPreferencesHandler.getSessionId(), token);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
