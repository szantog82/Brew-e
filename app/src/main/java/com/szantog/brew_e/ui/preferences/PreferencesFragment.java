package com.szantog.brew_e.ui.preferences;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.szantog.brew_e.common.AppButtonIdCollection;
import com.szantog.brew_e.R;
import com.szantog.brew_e.common.SharedPreferencesHandler;
import com.szantog.brew_e.databinding.PreferencesLayoutBinding;
import com.szantog.brew_e.domain.User;
import com.szantog.brew_e.ui.MainViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class PreferencesFragment extends Fragment {

    private User user;
    private MainViewModel mainViewModel;
    private PreferencesViewModel preferencesViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        PreferencesLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.preferences_layout, container, false);
        user = new User();
        user.setId(0);
        binding.setUser(user);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        preferencesViewModel = new ViewModelProvider(requireActivity()).get(PreferencesViewModel.class);

        SharedPreferencesHandler sharedPreferencesHandler = new SharedPreferencesHandler(getActivity());
        user.setUser(sharedPreferencesHandler.getUserData());

        Button okButton = view.findViewById(R.id.preferences_save_button);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedPreferencesHandler.setUserData(user);
                Toast.makeText(getActivity(), "A ment??s siker??lt!", Toast.LENGTH_LONG).show();
                preferencesViewModel.uploadUpdatedUserData(sharedPreferencesHandler.getSessionId(), sharedPreferencesHandler.getUserData());
                mainViewModel.setClickedButtonId(AppButtonIdCollection.PREFERENCESFRAGMENT_SAVE_BUTTON_ID);
            }
        });
    }
}
