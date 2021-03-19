package com.szantog.brew_e;

import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

interface ConnectionTesterCallback {
    void connectionTesterResult(boolean success);
}

interface ConnectionLoginCallback {
    void connectionLoginResult(boolean success);
}


public class ConnectionHandler {

    public static void testConnection(Context context, ConnectionTesterCallback connectionTesterCallback) {

        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(context);
        String session_id = sharedPreferencesHandler.getSessionId();
        if (session_id.length() < 1) {
            if (connectionTesterCallback != null) {
                connectionTesterCallback.connectionTesterResult(false);
            }
        }
        Call<User> call = RetrofitClient.getInstance().testUserConnection("PHPSESSID=" + session_id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (user.getLogin() != null) {
                    sharedPreferencesHandler.setUserData(user);
                    connectionTesterCallback.connectionTesterResult(true);
                } else {
                    connectionTesterCallback.connectionTesterResult(false);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                connectionTesterCallback.connectionTesterResult(false);
            }
        });
    }

    public static void logoutUser(Context context) {
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(context);
        String session_id = sharedPreferencesHandler.getSessionId();
        Call<Void> call = RetrofitClient.getInstance().logoutUser("PHPSESSID=" + session_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                sharedPreferencesHandler.clearUserData();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public static void loginUser(Context context, String login, String password, ConnectionLoginCallback connectionLoginCallback) {
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(context);
        Call<LoginResult> call = RetrofitClient.getInstance().loginUser(login, password);
        call.enqueue(new Callback<LoginResult>() {
            @Override
            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                LoginResult loginResult = response.body();
                if (loginResult.getSession_id().length() > 0) {
                    sharedPreferencesHandler.setLoginData(login, loginResult.getSession_id());
                    testConnection(context, new ConnectionTesterCallback() {
                        @Override
                        public void connectionTesterResult(boolean success) {
                            if (success) {
                                connectionLoginCallback.connectionLoginResult(true);
                            }
                        }
                    });
                } else {
                    connectionLoginCallback.connectionLoginResult(false);
                }
            }

            @Override
            public void onFailure(Call<LoginResult> call, Throwable t) {
                connectionLoginCallback.connectionLoginResult(false);
            }
        });
    }
}
