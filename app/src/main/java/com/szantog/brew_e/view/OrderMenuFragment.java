package com.szantog.brew_e.view;

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

import com.szantog.brew_e.R;
import com.szantog.brew_e.SharedPreferencesHandler;
import com.szantog.brew_e.model.DrinkItem;
import com.szantog.brew_e.model.User;
import com.szantog.brew_e.view.adapter.OrderMenuExpandableAdapter;
import com.szantog.brew_e.view.dialog.OrderSummarizeDialog;
import com.szantog.brew_e.viewmodel.MainViewModel;
import com.szantog.brew_e.viewmodel.RetrofitListViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class OrderMenuFragment extends Fragment implements View.OnClickListener, ExpandableListView.OnChildClickListener, OrderSummarizeDialog.OrderSummarizeDialogCallback {

    private List<DrinkItem> drinkItems = new ArrayList<>();
    private ExpandableListView menuListListView;
    private OrderMenuExpandableAdapter orderMenuExpandableAdapter;
    private MainViewModel mainViewModel;
    private RetrofitListViewModel retrofitListViewModel;

    private TextView titleText;
    private TextView bucketItemCountTextView;
    private TextView bucketSumTextView;
    private Button orderButton;

    private String shopName;
    private User user;
    private OrderSummarizeDialog orderSummarizeDialog;

    private List<DrinkItem> bucket;

    private boolean is_logged_in;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_menu_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bucketItemCountTextView = view.findViewById(R.id.order_menu_bucket_item_count_text);
        bucketSumTextView = view.findViewById(R.id.order_menu_bucket_item_sum_text);
        orderButton = view.findViewById(R.id.order_menu_order_button);
        orderButton.setOnClickListener(this);

        titleText = view.findViewById(R.id.order_menu_title_text);
        menuListListView = view.findViewById(R.id.order_menu_list_listview);
        bucket = new ArrayList<>();
        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getActivity());

        if (sharedPreferencesHandler.getSessionId().length() > 1) {
            orderButton.setEnabled(true);
            is_logged_in = true;
            user = sharedPreferencesHandler.getUserData();
        } else {
            is_logged_in = false;
        }
        orderMenuExpandableAdapter = new OrderMenuExpandableAdapter(getActivity(), drinkItems);
        menuListListView.setAdapter(orderMenuExpandableAdapter);
        menuListListView.setOnChildClickListener(this);
        for (int i = 0; i < orderMenuExpandableAdapter.getGroupCount(); i++) {
            menuListListView.expandGroup(i);
        }

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        retrofitListViewModel = new ViewModelProvider((requireActivity())).get(RetrofitListViewModel.class);
        retrofitListViewModel.getDrinkMenuItems().observe(getViewLifecycleOwner(), new Observer<List<DrinkItem>>() {
            @Override
            public void onChanged(List<DrinkItem> items) {
                if (items.size() > 0) {
                    drinkItems.clear();
                    drinkItems.addAll(items);
                    shopName = items.get(0).getShop_name();
                    titleText.setText(shopName + " kínálata");
                } else {
                    titleText.setText("Nincs még itallap feltöltve");
                }
                orderMenuExpandableAdapter.updateDataSet(items);
                for (int i = 0; i < orderMenuExpandableAdapter.getGroupCount(); i++) {
                    menuListListView.expandGroup(i);
                }
            }
        });
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
                orderSummarizeDialog.setData(shopName, user);
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
        if (orderSummarizeDialog != null) {
            orderSummarizeDialog.dismiss();
        }
        mainViewModel.setBucketForOrder(bucket);
    }
}
