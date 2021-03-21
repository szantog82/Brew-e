package com.szantog.brew_e;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class PrevOrdersAlertDialog extends AlertDialog.Builder {

    public PrevOrdersAlertDialog(@NonNull Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.prev_orders_dialog_layout, null);
        setView(view);

        ArrayList<Long> dates = new ArrayList<>();
        dates.add((long) 1615285964);
        dates.add((long) 1615885964);
        dates.add((long) 1613885964);

        ArrayList<String>[] entries = new ArrayList[3];
        entries[0] = new ArrayList<>();
        entries[1] = new ArrayList<>();
        entries[2] = new ArrayList<>();
        entries[0].add("Kávé");
        entries[0].add("Tej");
        entries[1].add("Kávé");
        entries[1].add("Cappucino");
        entries[1].add("Tea");
        entries[2].add("Sör");

        ExpandableListView expandableListView = view.findViewById(R.id.prev_orders_dialog_expandable_list);
        PrevOrdersExpandableAdapter adapter = new PrevOrdersExpandableAdapter(context, dates, entries);
        expandableListView.setAdapter(adapter);
        for (int position = 1; position <= dates.size(); position++) {
            expandableListView.expandGroup(position - 1);
        }
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }
}
