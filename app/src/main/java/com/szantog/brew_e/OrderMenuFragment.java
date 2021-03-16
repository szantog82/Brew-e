package com.szantog.brew_e;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OrderMenuFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<DrinkMenu> drinkMenus = new ArrayList<>();
    private MenuListViewAdapter menuListViewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView menuListListView = view.findViewById(R.id.order_menu_list_listview);

        menuListViewAdapter = new MenuListViewAdapter(getActivity(), drinkMenus);
        menuListListView.setAdapter(menuListViewAdapter);
        menuListListView.setOnItemClickListener(this);

        Button orderbtn = view.findViewById(R.id.order_button);
        orderbtn.setOnClickListener(this);
    }

    public void addMenuItems(List<DrinkMenu> drinkMenus) {
        this.drinkMenus.clear();
        this.drinkMenus.addAll(drinkMenus);
        menuListViewAdapter.notifyDataSetChanged();
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
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        String[] items = new String[]{"Tejjel (+100 Ft)", "Csokival (+200Ft)", "Cukorral (+50Ft)", "Magában (+0Ft)"};
        new AlertDialog.Builder(getActivity())
                .setTitle("Válassz kiegészítőket")
                .setSingleChoiceItems(items, 1, null)
                .setPositiveButton("Kosárba", null)
                .setNegativeButton("Mégse", null)
                .show();
    }
}
