package com.szantog.brew_e;

import android.content.Context;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

import static com.szantog.brew_e.MainController.CHANNEL_ID;

public class Socketing {

    private static int NOTIFICATION_ID = 1;
    public static boolean isConnected = false;

    public static void start(Context context) {
        Log.e("Socketing", "socketing starts");
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(context);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID).setSmallIcon(R.drawable.coffee_beans_32x32);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        if (sharedPreferencesHandler.getSessionId().length() > 10) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url("ws://brewe-websocket.herokuapp.com/ss?auth=" + sharedPreferencesHandler.getSessionId().substring(0, 7)).build();
            WebSocket ws = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    super.onOpen(webSocket, response);
                    isConnected = true;
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
                    isConnected = false;
                }
            });
            client.dispatcher().executorService().shutdown();
        }
    }
}
