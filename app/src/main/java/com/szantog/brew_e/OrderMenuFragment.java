package com.szantog.brew_e;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class OrderMenuFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnChildClickListener, OrderSummarizeDialog.OrderSummarizeDialogCallback {

    interface OrderMenuFragmentCallback {
        void OrderMenuFragmentOrderSubmitted(List<DrinkItem> bucket);
    }

    private OrderMenuFragmentCallback orderMenuFragmentCallback;

    private List<DrinkItem> drinkItems = new ArrayList<>();
    private OrderMenuExpandableAdapter orderMenuExpandableAdapter;

    private TextView bucketItemCountTextView;
    private TextView bucketSumTextView;
    private Button orderButton;

    private OrderSummarizeDialog orderSummarizeDialog;

    private List<DrinkItem> bucket;

    private boolean is_logged_in;

    public OrderMenuFragment(List<DrinkItem> drinkItems, OrderMenuFragmentCallback orderMenuFragmentCallback) {
        this.drinkItems = drinkItems;
        this.orderMenuFragmentCallback = orderMenuFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getActivity());
        if (sharedPreferencesHandler.getUserData().getLogin().length() > 1) {
            is_logged_in = true;
        } else {
            is_logged_in = false;
        }

        bucketItemCountTextView = view.findViewById(R.id.order_menu_bucket_item_count_text);
        bucketSumTextView = view.findViewById(R.id.order_menu_bucket_item_sum_text);
        orderButton = view.findViewById(R.id.order_menu_order_button);
        orderButton.setOnClickListener(this);

        TextView titleText = view.findViewById(R.id.order_menu_title_text);
        if (drinkItems.size() > 0) {
            titleText.setText(drinkItems.get(0).getShop_name() + " kínálata");
        } else {
            titleText.setText("Nincs még itallap feltöltve");
        }
        ExpandableListView menuListListView = view.findViewById(R.id.order_menu_list_listview);
        bucket = new ArrayList<>();

        orderMenuExpandableAdapter = new OrderMenuExpandableAdapter(getActivity(), drinkItems);
        menuListListView.setAdapter(orderMenuExpandableAdapter);
        menuListListView.setOnChildClickListener(this);
        for (int i = 0; i < orderMenuExpandableAdapter.getGroupCount(); i++) {
            menuListListView.expandGroup(i);
        }
    }

    private void updateBucket() {
        bucketItemCountTextView.setText(bucket.size() + " tétel");
        int sum = 0;
        for (DrinkItem d : bucket) {
            sum += d.getItem_price();
        }
        bucketSumTextView.setText(sum + "Ft");
        if (bucket.size() > 0 && is_logged_in) {
            orderButton.setEnabled(true);
        }
    }

    private void notLoggedInMessage() {
        Toast.makeText(getActivity(), "A rendeléshez jelentkezz be!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.order_menu_order_button) {
            if (is_logged_in) {
                orderSummarizeDialog = new OrderSummarizeDialog(getActivity(), this);
                orderSummarizeDialog.setBucket(bucket);
                orderSummarizeDialog.show();
            } else {
                notLoggedInMessage();
            }
        }
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (is_logged_in) {
            DrinkItem selectedItem = null;
            for (DrinkItem d : drinkItems) {
                if (d.getId() == id) {
                    selectedItem = d;
                }
            }
            DrinkItem finalSelectedItem = selectedItem;
            new AlertDialog.Builder(getActivity())
                    .setTitle("Ezt választottad: " + selectedItem.getItem_name())
                    .setPositiveButton("Kosárba", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            bucket.add(finalSelectedItem);
                            updateBucket();
                        }
                    })
                    .setNegativeButton("Mégse", null)
                    .show();
            return false;
        } else {
            notLoggedInMessage();
            return false;
        }
    }

    @Override
    public void orderSubmitted() {
        if (orderMenuFragmentCallback != null) {
            orderMenuFragmentCallback.OrderMenuFragmentOrderSubmitted(bucket);
        }
    }
}
