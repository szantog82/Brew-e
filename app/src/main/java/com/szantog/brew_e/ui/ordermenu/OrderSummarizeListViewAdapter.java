package com.szantog.brew_e.ui.ordermenu;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.szantog.brew_e.R;
import com.szantog.brew_e.domain.DrinkItem;

import java.util.List;

public class OrderSummarizeListViewAdapter extends BaseAdapter {

    private Context context;
    private List<DrinkItem> bucket;

    public OrderSummarizeListViewAdapter(Context context, List<DrinkItem> bucket) {
        this.context = context;
        this.bucket = bucket;
    }

    @Override
    public int getCount() {
        return bucket.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.order_menu_listitem_child_layout, viewGroup, false);
        }
        TextView nameTextView = view.findViewById(R.id.order_menu_listitem_name);
        TextView priceTextView = view.findViewById(R.id.order_menu_listitem_price_text);
        ImageView imageView = view.findViewById(R.id.order_menu_listitem_img);
        nameTextView.setText(bucket.get(i).getItem_name());
        priceTextView.setText(bucket.get(i).getItem_price() + " Ft");
        try {
            int index = bucket.get(i).getItem_image().indexOf("base64,");
            String base64_img = bucket.get(i).getItem_image().substring(index + 7);
            Glide.with(context)
                    .load(Base64.decode(base64_img, Base64.DEFAULT))
                    .into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }
}
