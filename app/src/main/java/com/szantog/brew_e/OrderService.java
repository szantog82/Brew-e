package com.szantog.brew_e;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.szantog.brew_e.MainController.CHANNEL_ID;

public class OrderService extends Service {

    private NotificationManagerCompat notificationManager;
    private NotificationCompat.Builder builder;
    private static int NOTIFICATION_ID = 1;

    private WebSocket ws;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(this);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.coffee_beans_32x32);
        notificationManager = NotificationManagerCompat.from(this);


        if (sharedPreferencesHandler.getSessionId().length() > 10) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("ws://brewe-websocket.herokuapp.com/ss?auth=" + sharedPreferencesHandler.getSessionId().substring(0, 7)).build();
            ws = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    super.onMessage(webSocket, text);
                    String arr[] = text.split("&&");
                    if (arr.length > 1) {
                        builder.setContentTitle(arr[0])
                                .setContentText(arr[1]);
                    } else {
                        builder.setContentTitle("Brew-e üzenet")
                                .setContentText(arr[0]);
                    }
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    notificationManager.notify(++NOTIFICATION_ID, builder.build());
                }

                @Override
                public void onClosing(WebSocket webSocket, int code, String reason) {
                    super.onClosing(webSocket, code, reason);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    super.onClosed(webSocket, code, reason);
                }
            });
            client.dispatcher().executorService().shutdown();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            ws.close(1000, "Service halted");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
