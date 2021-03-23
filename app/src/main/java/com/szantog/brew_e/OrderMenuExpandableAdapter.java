package com.szantog.brew_e;

import android.content.Context;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class OrderMenuExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groups;
    private List<DrinkItem>[] orderedList;

    public OrderMenuExpandableAdapter(Context context, List<DrinkItem> drinkItems) {
        this.context = context;
        groups = new ArrayList<>();

        for (DrinkItem drinkItem : drinkItems) {
            if (groups.indexOf(drinkItem.getItem_group()) < 0) {
                groups.add(drinkItem.getItem_group());
            }
        }

        orderedList = new ArrayList[groups.size()];
        for (int i = 0; i < orderedList.length; i++) {
            orderedList[i] = new ArrayList<>();
        }

        for (DrinkItem drinkItem : drinkItems) {
            orderedList[groups.indexOf(drinkItem.getItem_group())].add(drinkItem);
        }
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return orderedList[i].size();
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i1) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return orderedList[i].get(i1).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.order_menu_listitem_group_layout, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.order_menu_listitem_group_text);
        textView.setText(groups.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.order_menu_listitem_child_layout, viewGroup, false);
        }
        TextView nameTextView = view.findViewById(R.id.order_menu_listitem_name);
        TextView priceTextView = view.findViewById(R.id.order_menu_listitem_price_text);
        ImageView imageView = view.findViewById(R.id.order_menu_listitem_img);
        nameTextView.setText(orderedList[i].get(i1).getItem_name());
        priceTextView.setText(orderedList[i].get(i1).getItem_price() + " Ft");
        Glide.with(context)
                .load(Base64.decode(orderedList[i].get(i1).getItem_image(), Base64.DEFAULT))
                .into(imageView);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
