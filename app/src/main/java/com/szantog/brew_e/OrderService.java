package com.szantog.brew_e;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.TimeUnit;

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
    private static final String FOREGROUND_CHANNEL_ID = "2013436";
    private Notification foregroundNotification;

    private static final String CLOSE_SERVICE_INTENT_ACTION = "brewe.szantog.com.CLOSE_SERVICE_INTENT_ACTION";

    private SharedPreferencesHandler sharedPreferencesHandler;

    private OkHttpClient client;
    private Request request;

    //private static final String WEBSOCKET_URL = "ws://bold-wind-museum.glitch.me/";
    private static final String WEBSOCKET_URL = "ws://brewe-websocket.herokuapp.com/";
    private WebSocket ws;
    private boolean isSocketActive = false;
    private boolean terminateSocket = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction() != null && intent.getAction().equals(CLOSE_SERVICE_INTENT_ACTION)) {
            if (ws != null) {
                ws.close(1001, "Connection halted");
            }
        }
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferencesHandler = new SharedPreferencesHandler(this);

        builder = new NotificationCompat.Builder(this, CHANNEL_ID).setSmallIcon(R.drawable.coffee_beans_32x32);
        notificationManager = NotificationManagerCompat.from(this);

        if (sharedPreferencesHandler.getUserEmail().length() > 3) {
            client = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .build();
            request = new Request.Builder().url(WEBSOCKET_URL + "service?auth=" + sharedPreferencesHandler.getUserEmail()).build();
        }
        if (!isSocketActive) {
            startWebSocket();
        }
    }

    private void startWebSocket() {
        ws = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                Log.d("OrderService", "Socket onOpen");
                isSocketActive = true;
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
                isSocketActive = false;
                Log.d("OrderService", "Socket onClosing");
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                super.onClosed(webSocket, code, reason);
                isSocketActive = false;
                Log.d("OrderService", "Socket onClosed");
                if (!terminateSocket) {
                    startWebSocket();
                }
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, @Nullable Response response) {
                super.onFailure(webSocket, t, response);
                isSocketActive = false;
                Log.d("OrderService", "Socket onFailure");
                if (!terminateSocket) {
                    startWebSocket();
                }
            }
        });
        client.dispatcher().executorService().shutdown();

        setForegroundMainNotification();
        startForeground(1, foregroundNotification);
    }

    private void setForegroundMainNotification() {
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(FOREGROUND_CHANNEL_ID, "Brew-e", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, FOREGROUND_CHANNEL_ID).setSmallIcon(R.drawable.coffee_beans_32x32);
        foregroundNotification = builder
                .setContentTitle("Brew-e")
                .setContentText("Kapcsolódás a szerverhez")
                .build();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            terminateSocket = true;
            ws.close(1000, "Service halted");
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}
