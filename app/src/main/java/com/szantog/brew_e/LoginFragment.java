package com.szantog.brew_e;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

interface LoginFragmentCallback {
    void loginFragmentButtonClicked(boolean success);
}

public class LoginFragment extends Fragment {

    private LoginFragmentCallback loginFragmentCallback;

    public LoginFragment(LoginFragmentCallback loginFragmentCallback) {
        this.loginFragmentCallback = loginFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button loginButton = view.findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginFragmentCallback != null) {
                    loginFragmentCallback.loginFragmentButtonClicked(true);
                }
            }
        });
    }
}
