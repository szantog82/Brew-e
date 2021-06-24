package com.szantog.brew_e.ui.ordersent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.szantog.brew_e.domain.DrinkItem;
import com.szantog.brew_e.R;
import com.szantog.brew_e.ui.ordermenu.OrderSummarizeListViewAdapter;
import com.szantog.brew_e.ui.MainViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class OrderSentFragment extends Fragment {

    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_sent_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView shopNameTextView = view.findViewById(R.id.order_sent_shop_name);
        TextView sumTextView = view.findViewById(R.id.order_sent_sum_textview);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        mainViewModel.getBucketForOrder().observe(getViewLifecycleOwner(), new Observer<List<DrinkItem>>() {
            @Override
            public void onChanged(List<DrinkItem> bucket) {
                shopNameTextView.setText(bucket.get(0).getShop_name());

                int sum = 0;
                for (DrinkItem d : bucket) {
                    sum += d.getItem_price();
                }
                sumTextView.setText("Összesen: " + sum + "Ft");

                ListView listView = view.findViewById(R.id.order_sent_listview);
                OrderSummarizeListViewAdapter adapter = new OrderSummarizeListViewAdapter(getActivity(), bucket);
                listView.setAdapter(adapter);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mainViewModel.setBucketForOrder(new ArrayList<>());
    }
}
