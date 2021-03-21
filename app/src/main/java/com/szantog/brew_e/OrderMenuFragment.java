package com.szantog.brew_e;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OrderMenuFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnChildClickListener {

    private List<DrinkMenu> drinkMenus = new ArrayList<>();
    private OrderMenuExpandableAdapter orderMenuExpandableAdapter;

    public OrderMenuFragment(List<DrinkMenu> drinkMenus) {
        this.drinkMenus = drinkMenus;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView titleText = view.findViewById(R.id.order_menu_title_text);
        if (drinkMenus.size() > 0) {
            titleText.setText(drinkMenus.get(0).getShop_id() + " kínálata");
        } else {
            titleText.setText("Nincs még itallap feltöltve");
        }
        ExpandableListView menuListListView = view.findViewById(R.id.order_menu_list_listview);

        orderMenuExpandableAdapter = new OrderMenuExpandableAdapter(getActivity(), drinkMenus);
        menuListListView.setAdapter(orderMenuExpandableAdapter);
        menuListListView.setOnChildClickListener(this);
        for (int i = 0; i < orderMenuExpandableAdapter.getGroupCount(); i++) {
            menuListListView.expandGroup(i);
        }

        Button orderbtn = view.findViewById(R.id.order_button);
        orderbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.order_button) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Megerősítés")
                    .setMessage(Html.fromHtml("<u>Biztos, hogy megrendeljük?</u>"))
                    .setPositiveButton(android.R.string.yes, null)
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        // String[] items = new String[]{"Tejjel (+100 Ft)", "Csokival (+200Ft)", "Cukorral (+50Ft)", "Magában (+0Ft)"};
        DrinkMenu selectedItem = null;
        for (DrinkMenu d : drinkMenus) {
            if (d.getId() == id) {
                selectedItem = d;
            }
        }
        new AlertDialog.Builder(getActivity())
                .setTitle("Ezt választottad: " + selectedItem.getItem_name())
                // .setSingleChoiceItems(items, 1, null)
                .setPositiveButton("Kosárba", null)
                .setNegativeButton("Mégse", null)
                .show();
        return false;
    }
}
