package com.szantog.brew_e.ui.blog;

import android.util.Log;

import com.szantog.brew_e.clients.brewe.RetrofitClient;
import com.szantog.brew_e.domain.BlogItem;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BlogViewModel extends ViewModel {

    private final MutableLiveData<List<BlogItem>> blogItems = new MutableLiveData<>();

    public void downloadBlogs(int shopId) {
        Call<List<BlogItem>> call = RetrofitClient.getInstance().getBlogs(shopId);
        call.enqueue(new Callback<List<BlogItem>>() {
            @Override
            public void onResponse(Call<List<BlogItem>> call, Response<List<BlogItem>> response) {
                blogItems.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<BlogItem>> call, Throwable t) {
                Log.e("downloadBlog failure", t.getMessage());
            }
        });
    }


    public LiveData<List<BlogItem>> getBlogs() {
        return blogItems;
    }
}
