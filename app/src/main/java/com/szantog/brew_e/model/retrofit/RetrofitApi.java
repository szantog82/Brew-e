package com.szantog.brew_e.model.retrofit;

import com.szantog.brew_e.model.BlogItem;
import com.szantog.brew_e.model.CoffeeShop;
import com.szantog.brew_e.model.DrinkItem;
import com.szantog.brew_e.model.ResponseModel;
import com.szantog.brew_e.model.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RetrofitApi {
    public String BASE_URL = "http://www.szantog82.nhely.hu";

    @GET("get_shops")
    Call<List<CoffeeShop>> getCoffeeShops();

    @GET("get_drink_menu/{shop_id}")
    Call<List<DrinkItem>> getDrinkMenu(@Path("shop_id") int shop_id);

    @GET("get_blog/{shop_id}")
    Call<List<BlogItem>> getBlogs(@Path("shop_id") int shop_id);

    @FormUrlEncoded
    @POST("login_user")
    Call<ResponseModel> loginUser(@Field("login") String login, @Field("password") String password);

    @FormUrlEncoded
    @POST("register_user")
    Call<ResponseModel> registerUser(@Field("user") String user);

    @FormUrlEncoded
    @POST("modify_user_params")
    Call<Void> modifyUser(@Header("Cookie") String session_id, @Field("user") String user);

    @POST("test_user_connection")
    Call<User> testUserConnection(@Header("Cookie") String session_id);

    @POST("logout_user")
    Call<Void> logoutUser(@Header("Cookie") String session_id);

    @FormUrlEncoded
    @POST("upload_order")
    Call<ResponseModel> uploadOrder(@Header("Cookie") String session_id, @Field("bucket") String data);
}
