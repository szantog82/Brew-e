package com.szantog.brew_e.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.szantog.brew_e.viewmodel.MainViewModel;
import com.szantog.brew_e.R;
import com.szantog.brew_e.viewmodel.RetrofitListViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


public class LoginFragment extends Fragment {

    public static final int SUBMIT_BUTTON_ID = 300;
    public static final int REGISTER_BUTTON_ID = 301;
    public static final int FORGOT_PASSWORD_BUTTON_ID = 302;

    private MainViewModel mainViewModel;
    private RetrofitListViewModel retrofitListViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainViewModel = new ViewModelProvider((requireActivity())).get(MainViewModel.class);
        retrofitListViewModel = new ViewModelProvider((requireActivity())).get(RetrofitListViewModel.class);

        Button loginButton = view.findViewById(R.id.login_button);
        TextView registerTextView = view.findViewById(R.id.login_register_text);
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mainViewModel.setClickedButtonId(REGISTER_BUTTON_ID);
            }
        });

        EditText loginText = view.findViewById(R.id.login_username_edittext);
        EditText passwordText = view.findViewById(R.id.login_password_edittext);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitListViewModel.loginUser(loginText.getText().toString(), passwordText.getText().toString());
            }
        });
    }
}
