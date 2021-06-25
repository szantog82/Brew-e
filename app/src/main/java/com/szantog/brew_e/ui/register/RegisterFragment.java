package com.szantog.brew_e.ui.register;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.szantog.brew_e.R;
import com.szantog.brew_e.clients.brewe.dtos.AuthResponse;
import com.szantog.brew_e.common.AppButtonIdCollection;
import com.szantog.brew_e.common.SharedPreferencesHandler;
import com.szantog.brew_e.domain.User;
import com.szantog.brew_e.ui.MainController;
import com.szantog.brew_e.ui.MainViewModel;
import com.szantog.brew_e.databinding.RegisterLayoutBinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class RegisterFragment extends Fragment {

    private User user;
    private MainViewModel mainViewModel;
    private RegistrationViewModel registrationViewModel;
    private SharedPreferencesHandler sharedPreferencesHandler;

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

        sharedPreferencesHandler=new SharedPreferencesHandler(getActivity());

        mainViewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);

        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        registrationViewModel.getRegistrationresponse().observe(getViewLifecycleOwner(), new Observer<AuthResponse>() {
            @Override
            public void onChanged(AuthResponse authResponse) {
                if (authResponse.getSuccess() == 1) {
                    sharedPreferencesHandler.setSessionId(authResponse.getSession_id());
                    Toast.makeText(getActivity(), "Sikeres regisztráció", Toast.LENGTH_LONG).show();
                    mainViewModel.testConnection(authResponse.getSession_id());
                } else {
                    Toast.makeText(getActivity(), "Sikertelen regisztráció!", Toast.LENGTH_LONG).show();
                }
            }
        });


        Button submitButton = view.findViewById(R.id.register_submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getLogin() == null || user.getPassword() == null || user.getEmail() == null
                        || user.getFamily_name() == null || user.getFirst_name() == null) {
                    Toast.makeText(getActivity(), "Minden mezőt ki kell tölteni", Toast.LENGTH_LONG).show();
                } else {
                    registrationViewModel.uploadUserRegistration(user);
                    mainViewModel.setRegisterUserData(user);
                    mainViewModel.setClickedButtonId(AppButtonIdCollection.LOAD_OPENSCREENFRAGMENT_ID);
                }
            }
        });
    }
}
