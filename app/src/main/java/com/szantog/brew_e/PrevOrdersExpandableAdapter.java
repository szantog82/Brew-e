package com.szantog.brew_e;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;

public class PrevOrdersExpandableAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Long> dates;
    private ArrayList<String>[] entries;

    public PrevOrdersExpandableAdapter(Context context, ArrayList<Long> dates, ArrayList<String>[] entries) {
        this.context = context;
        this.dates = dates;
        this.entries = entries;
    }

    @Override
    public int getGroupCount() {
        return dates.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return entries[i].size();
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
            view = LayoutInflater.from(context).inflate(R.layout.prev_orders_expandable_listitem, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.prev_orders_expandable_listitem_text);
        textView.setText(String.valueOf(dates.get(i)));
        textView.setBackgroundColor(Color.YELLOW);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.prev_orders_expandable_listitem, viewGroup, false);
        }
        TextView textView = view.findViewById(R.id.prev_orders_expandable_listitem_text);
        textView.setText(entries[i].get(i1));
        textView.setBackgroundColor(Color.WHITE);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
