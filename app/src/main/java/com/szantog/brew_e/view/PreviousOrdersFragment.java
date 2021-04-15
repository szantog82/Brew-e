package com.szantog.brew_e.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.szantog.brew_e.model.OrderedItem;
import com.szantog.brew_e.R;
import com.szantog.brew_e.view.adapter.PrevOrdersExpandableAdapter;
import com.szantog.brew_e.viewmodel.DatabaseViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class PreviousOrdersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.prev_orders_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ExpandableListView expandableListView = view.findViewById(R.id.prev_orders_expandable_list);
        PrevOrdersExpandableAdapter adapter = new PrevOrdersExpandableAdapter(getActivity(), new ArrayList<>());
        expandableListView.setAdapter(adapter);

        DatabaseViewModel databaseViewModel = new ViewModelProvider(requireActivity()).get(DatabaseViewModel.class);
        databaseViewModel.getAllOrderedItems().observe(getViewLifecycleOwner(), new Observer<List<OrderedItem>>() {
            @Override
            public void onChanged(List<OrderedItem> orderedItems) {
                adapter.setOrderedItems(orderedItems);
                for (int position = 1; position <= adapter.getGroupCount(); position++) {
                    expandableListView.expandGroup(position - 1);
                }
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                return true;
            }
        });
    }
}
