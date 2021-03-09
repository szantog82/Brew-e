package com.szantog.brew_e;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MenuListViewAdapter extends BaseAdapter {

    private Context context;
    private String[] drinks;

    public MenuListViewAdapter(Context context, String[] drinks) {
        this.context = context;
        this.drinks = drinks;
    }

    @Override
    public int getCount() {
        return drinks.length;
    }

    @Override
    public Object getItem(int i) {
        return drinks[i];
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
        TextView tv = view.findViewById(R.id.order_menu_listitem_name);
        tv.setText(drinks[i]);
        return view;
    }
}

