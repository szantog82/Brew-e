package com.szantog.brew_e;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

interface OpenScreenFragmentCallback {
    void openScreenFragmentButtonClicked(int buttonId);
}

public class OpenScreenFragment extends Fragment {


    public static final int LOGIN_BUTTON_ID = 100;
    public static final int BROWSE_BUTTON_ID = 101;
    public static final int BLOG_BUTTON_ID = 102;
    private OpenScreenFragmentCallback openScreenFragmentCallback;

    public OpenScreenFragment(OpenScreenFragmentCallback openScreenFragmentCallback) {
        this.openScreenFragmentCallback = openScreenFragmentCallback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.open_screen_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LinearLayout innerLinearLayout = view.findViewById(R.id.open_layout_inner_linearlayout);
        for (int i = 0; i < innerLinearLayout.getChildCount(); i++) {
            TextView tv = (TextView) innerLinearLayout.getChildAt(i);
            tv.setOnClickListener(this::buttonClicked);
        }
    }

    public void buttonClicked(View v) {
        int id = 0;
        switch (v.getId()) {
            case R.id.open_layout_login_text:
                id = LOGIN_BUTTON_ID;
                break;
            case R.id.open_layout_browse_text:
                id = BROWSE_BUTTON_ID;
                break;
            case R.id.open_layout_blog_text:
                id = BLOG_BUTTON_ID;
                break;
        }
        if (openScreenFragmentCallback != null) {
            openScreenFragmentCallback.openScreenFragmentButtonClicked(id);
        }
    }

}
