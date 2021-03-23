package com.szantog.brew_e;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class OrderSummarizeDialog extends AlertDialog.Builder {

    interface OrderSummarizeDialogCallback {
        void orderSubmitted();
    }

    private List<DrinkItem> bucket;
    private OrderSummarizeListViewAdapter adapter;

    public OrderSummarizeDialog(@NonNull Context context, OrderSummarizeDialogCallback orderSummarizeDialogCallback) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.order_summarize_layout, null);
        setView(view);

        bucket = new ArrayList<>();

        ListView listView = view.findViewById(R.id.order_summarize_listview);
        adapter = new OrderSummarizeListViewAdapter(context, bucket);
        TextView userTextView = view.findViewById(R.id.order_summarize_user_details);
        TextView shopTextView = view.findViewById(R.id.order_summarize_shop_details);

        listView.setAdapter(adapter);

        setPositiveButton("Megrendelés", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (orderSummarizeDialogCallback != null) {
                    orderSummarizeDialogCallback.orderSubmitted();
                }
            }
        });
    }

    public void setBucket(List<DrinkItem> bucket) {
        this.bucket.clear();
        this.bucket.addAll(bucket);
        adapter.notifyDataSetChanged();
    }
}

/*Rendelésnél: képet ne küldjük vissza a szerverre!
        Darabszám megjelölése!*/
