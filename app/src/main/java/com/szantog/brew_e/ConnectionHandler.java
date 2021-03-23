package com.szantog.brew_e;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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
    void connectionMenuResult(List<DrinkItem> drinkItems);
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
        Call<ResponseModel> call = RetrofitClient.getInstance().loginUser(login, password);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel responseModel = response.body();
                if (responseModel.getSession_id().length() > 0) {
                    sharedPreferencesHandler.setLoginData(login, responseModel.getSession_id());
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
            public void onFailure(Call<ResponseModel> call, Throwable t) {
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
                Log.e("downloadShopsForMap", t.getMessage());
            }
        });
    }

    public static void downloadMenu(Context context, int shop_id, ConnectionDownloadMenu connectionDownloadMenu) {
        Call<List<DrinkItem>> call = RetrofitClient.getInstance().getDrinkMenu(shop_id);
        call.enqueue(new Callback<List<DrinkItem>>() {
            @Override
            public void onResponse(Call<List<DrinkItem>> call, Response<List<DrinkItem>> response) {
                List<DrinkItem> drinkItems = response.body();
                if (connectionDownloadMenu != null) {
                    connectionDownloadMenu.connectionMenuResult(drinkItems);
                }
            }

            @Override
            public void onFailure(Call<List<DrinkItem>> call, Throwable t) {
                Log.e("downloadMenu failure", t.getMessage());
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
                Log.e("downloadBlogs failure", t.getMessage());
            }
        });
    }

    public static void uploadOrder(Context context, List<DrinkItem> bucket) {
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(context);
        String session_id = sharedPreferencesHandler.getSessionId();
        List<DrinkItemForUpload> bucketForUpload = new ArrayList<>();

        //convert DrinkItem to DrinkItemForUpload
        for (DrinkItem item : bucket) {
            boolean found = false;
            for (DrinkItemForUpload o : bucketForUpload) {
                if (o.getItem_id() == item.getId()) {
                    found = true;
                    o.setItem_count((o.getItem_count() + 1));
                    break;
                }
            }
            if (!found) {
                bucketForUpload.add(new DrinkItemForUpload(item.getId(), 1, item.getShop_id()));
            }
        }
        Gson gson = new GsonBuilder().setLenient().create();
        String json_string = gson.toJson(bucketForUpload);
        Call<ResponseModel> call = RetrofitClient.getInstance().uploadOrder("PHPSESSID=" + session_id, json_string);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> response) {
                ResponseModel r = response.body();
                if (r.getSuccess() == 1) {
                    //siker
                }
            }

            @Override
            public void onFailure(Call<ResponseModel> call, Throwable t) {
                t.printStackTrace();
                Log.e("uploadOrder", t.getMessage());

            }
        });
    }
}
