package com.szantog.brew_e.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.szantog.brew_e.model.OrderedItem;
import com.szantog.brew_e.R;

import java.util.ArrayList;
import java.util.List;

public class PrevOrdersExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> groups;
    private List<OrderedItem> structuredItems[];

    public PrevOrdersExpandableAdapter(Context context, List<OrderedItem> items) {
        this.context = context;

        groups = new ArrayList<>();
        structuredItems = new ArrayList[groups.size()];
        generateStructuredItems(items);
    }

    private void generateStructuredItems(List<OrderedItem> items) {
        for (OrderedItem item : items) {
            if (groups.indexOf(item.date) < 0) {
                groups.add(item.date);
            }
        }
        structuredItems = new ArrayList[groups.size()];

        for (int i = 0; i < structuredItems.length; i++) {
            structuredItems[i] = new ArrayList<>();
        }

        for (OrderedItem item : items) {
            structuredItems[groups.indexOf(item.date)].add(item);
        }
    }


    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return structuredItems[i].size();
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
        return 0;
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
        int sum = 0;
        for (OrderedItem item : structuredItems[i]) {
            sum += item.price;
        }
        textView.setText(groups.get(i) + "(" + sum + "Ft)");
        textView.setBackgroundColor(Color.YELLOW);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.prev_orders_expandable_child_listitem, viewGroup, false);
        }
        TextView nameTextView = view.findViewById(R.id.prev_orders_expandable_listitem_name_text);
        TextView priceTextView = view.findViewById(R.id.prev_orders_expandable_listitem_price_text);
        nameTextView.setText(structuredItems[groupPosition].get(childPosition).name + " (" +
                structuredItems[groupPosition].get(childPosition).shop_name + ")");
        priceTextView.setText(structuredItems[groupPosition].get(childPosition).price + "Ft");
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    public void setOrderedItems(List<OrderedItem> items) {
        generateStructuredItems(items);
        notifyDataSetChanged();
    }
}
