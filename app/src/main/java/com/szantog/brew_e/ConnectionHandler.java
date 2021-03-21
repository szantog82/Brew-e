package com.szantog.brew_e;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

interface ConnectionTesterCallback {
    void connectionTesterResult(boolean success);
}

interface ConnectionLoginCallback {
    void connectionLoginResult(boolean success);
}

interface ConnectionLogoutCallback {
    void connectionLogoutResult(boolean success);
}

interface ConnectionDownloadShopsForMapCallback {
    void connectionMapResult(List<CoffeeShop> coffeeShops);
}

interface ConnectionDownloadMenu {
    void connectionMenuResult(List<DrinkMenu> drinkMenus);
}

interface ConnectionDownloadBlogs {
    void connectionBlogsResult(List<BlogItem> blogList);
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
                }
                connectionTesterCallback.connectionTesterResult(true);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                connectionTesterCallback.connectionTesterResult(false);
            }
        });
    }

    public static void logoutUser(Context context, ConnectionLogoutCallback connectionLogoutCallback) {
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(context);
        String session_id = sharedPreferencesHandler.getSessionId();
        Call<Void> call = RetrofitClient.getInstance().logoutUser("PHPSESSID=" + session_id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (connectionLogoutCallback != null) {
                    connectionLogoutCallback.connectionLogoutResult(true);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                if (connectionLogoutCallback != null) {
                    connectionLogoutCallback.connectionLogoutResult(false);
                }
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

    public static void downloadShopsForMap(Context context, ConnectionDownloadShopsForMapCallback connectionDownloadShopsForMapCallback) {
        Call<List<CoffeeShop>> call = RetrofitClient.getInstance().getCoffeeShops();
        call.enqueue(new Callback<List<CoffeeShop>>() {
            @Override
            public void onResponse(Call<List<CoffeeShop>> call, Response<List<CoffeeShop>> response) {
                List<CoffeeShop> coffeeShopList = response.body();
                if (connectionDownloadShopsForMapCallback != null) {
                    connectionDownloadShopsForMapCallback.connectionMapResult(coffeeShopList);
                }
            }

            @Override
            public void onFailure(Call<List<CoffeeShop>> call, Throwable t) {

            }
        });
    }

    public static void downloadMenu(Context context, int shop_id, ConnectionDownloadMenu connectionDownloadMenu) {
        Call<List<DrinkMenu>> call = RetrofitClient.getInstance().getDrinkMenu(shop_id);
        call.enqueue(new Callback<List<DrinkMenu>>() {
            @Override
            public void onResponse(Call<List<DrinkMenu>> call, Response<List<DrinkMenu>> response) {
                List<DrinkMenu> drinkMenus = response.body();
                if (connectionDownloadMenu != null) {
                    connectionDownloadMenu.connectionMenuResult(drinkMenus);
                }
            }

            @Override
            public void onFailure(Call<List<DrinkMenu>> call, Throwable t) {

            }
        });
    }

    public static void downloadBlogs(Context context, int shop_id, ConnectionDownloadBlogs connectionDownloadBlogs) {
        Call<List<BlogItem>> call = RetrofitClient.getInstance().getBlogs(shop_id);
        call.enqueue(new Callback<List<BlogItem>>() {
            @Override
            public void onResponse(Call<List<BlogItem>> call, Response<List<BlogItem>> response) {
                if (connectionDownloadBlogs != null) {
                    connectionDownloadBlogs.connectionBlogsResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<BlogItem>> call, Throwable t) {

            }
        });
    }
}
