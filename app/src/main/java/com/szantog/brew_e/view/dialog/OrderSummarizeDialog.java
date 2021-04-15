package com.szantog.brew_e.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.szantog.brew_e.R;
import com.szantog.brew_e.model.DrinkItem;
import com.szantog.brew_e.model.User;
import com.szantog.brew_e.view.adapter.OrderSummarizeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class OrderSummarizeDialog extends AlertDialog {

    public interface OrderSummarizeDialogCallback {
        void orderSubmitted();
    }

    private List<DrinkItem> bucket;
    private OrderSummarizeListViewAdapter adapter;

    private TextView userTextView;
    private TextView shopTextView;

    public OrderSummarizeDialog(@NonNull Context context, OrderSummarizeDialogCallback orderSummarizeDialogCallback) {
        super(context, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        View view = LayoutInflater.from(context).inflate(R.layout.order_summarize_dialog_layout, null);
        setView(view);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        bucket = new ArrayList<>();

        ListView listView = view.findViewById(R.id.order_summarize_listview);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = screenHeight / 2;
        listView.setLayoutParams(params);
        adapter = new OrderSummarizeListViewAdapter(context, bucket);
        userTextView = view.findViewById(R.id.order_summarize_user_details);
        shopTextView = view.findViewById(R.id.order_summarize_shop_details);
        listView.setAdapter(adapter);

        Button orderButton = view.findViewById(R.id.order_summarize_order_btn);
        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderSummarizeDialogCallback != null) {
                    orderSummarizeDialogCallback.orderSubmitted();
                }
            }
        });
    }

    public void setData(String shopName, User user) {
        userTextView.setText("Megrendelő: " + user.getFamily_name() + " " + user.getFirst_name());
        shopTextView.setText("Kávézó: " + shopName);
    }

    public void setBucket(List<DrinkItem> bucket) {
        this.bucket.clear();
        this.bucket.addAll(bucket);
        adapter.notifyDataSetChanged();
    }
}