package com.szantog.brew_e;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MenuListViewAdapter extends BaseAdapter {

    private Context context;
    private List<DrinkMenu> drinkMenus;

    public MenuListViewAdapter(Context context, List<DrinkMenu> drinkMenus) {
        this.context = context;
        this.drinkMenus = drinkMenus;
    }

    @Override
    public int getCount() {
        return drinkMenus.size();
    }

    @Override
    public Object getItem(int i) {
        return drinkMenus.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.order_menu_listitem_layout, viewGroup, false);
        }
        TextView nameTextView = view.findViewById(R.id.order_menu_listitem_name);
        TextView priceTextView = view.findViewById(R.id.order_menu_listitem_price_text);
        ImageView imageView = view.findViewById(R.id.order_menu_listitem_img);
        nameTextView.setText(drinkMenus.get(i).getItem_name());
        priceTextView.setText(drinkMenus.get(i).getItem_price() + " Ft");
        Glide.with(context)
                .load(Base64.decode(drinkMenus.get(i).getItem_image(), Base64.DEFAULT))
                .into(imageView);
        return view;
    }
}

