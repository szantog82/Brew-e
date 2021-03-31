package com.szantog.brew_e;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.szantog.brew_e.databinding.RegisterLayoutBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class RegisterFragment extends Fragment {

    private User user;
    private MainViewModel mainViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RegisterLayoutBinding binding = DataBindingUtil.inflate(inflater, R.layout.register_layout, container, false);
        user = new User();
        binding.setUser(user);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        Button submitButton = view.findViewById(R.id.register_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getLogin() == null || user.getPassword() == null || user.getEmail() == null
                        || user.getFamily_name() == null || user.getFirst_name() == null) {
                    Toast.makeText(getActivity(), "Minden mezőt ki kell tölteni", Toast.LENGTH_LONG).show();
                } else {
                    mainViewModel.setRegisterUserData(user);
                }
            }
        });
    }
}
