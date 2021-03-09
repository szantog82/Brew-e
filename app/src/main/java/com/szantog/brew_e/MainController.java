package com.szantog.brew_e;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainController extends AppCompatActivity implements OpenScreenFragmentCallback, BrowseFragmentCallback, NavigationView.OnNavigationItemSelectedListener, LoginFragmentCallback {

    private FragmentTransaction fragmentTransaction;
    private OpenScreenFragment openScreenFragment;
    private BrowseFragment browseFragment;
    private LoginFragment loginFragment;
    private OrderMenuFragment orderMenuFragment;
    private BlogFragment blogFragment;

    private Fragment previousFragment;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView navHeaderNameTextView;

    private static boolean logged_in = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        navHeaderNameTextView = navigationHeaderView.findViewById(R.id.drawer_header_user_name);
        navHeaderNameTextView.setText("Vendég");

        openScreenFragment = new OpenScreenFragment(this);
        browseFragment = new BrowseFragment(this);
        loginFragment = new LoginFragment(this);
        orderMenuFragment = new OrderMenuFragment();
        blogFragment = new BlogFragment();

        previousFragment = openScreenFragment;
        changeFragment(openScreenFragment);
    }

    private void changeFragment(Fragment fragment) {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_fragment_placeholder, fragment);
        fragmentTransaction.commit();
    }

    private void updateNavigationView() {
        navigationView.getMenu().clear();
        if (logged_in) {
            navigationView.inflateMenu(R.menu.drawer_menu_logged_in);
            //  navigationView.setNavigationItemSelectedListener(this);
            navHeaderNameTextView.setText("Kovács Pistike");
        } else {
            navigationView.inflateMenu(R.menu.drawer_menu_not_logged_in);
            //  navigationView.setNavigationItemSelectedListener(this);
            navHeaderNameTextView.setText("Vendég");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        changeFragment(previousFragment);
    }

    @Override
    public void openScreenFragmentButtonClicked(int buttonId) {
        previousFragment = openScreenFragment;
        switch (buttonId) {
            case OpenScreenFragment.LOGIN_BUTTON_ID:
                changeFragment(loginFragment);
                break;
            case OpenScreenFragment.BROWSE_BUTTON_ID:
                changeFragment(browseFragment);
                break;
            case OpenScreenFragment.BLOG_BUTTON_ID:
                changeFragment(blogFragment);
                break;
        }
    }

    @Override
    public void browseFragmentButtonClicked(int buttonId, int shopId) {
        previousFragment = browseFragment;
        if (buttonId == BrowseFragment.MENU_BUTTON_ID) {
            changeFragment(orderMenuFragment);
        } else if (buttonId == BrowseFragment.BLOG_BUTTON_ID) {
            changeFragment(blogFragment);
        }
    }


    @Override
    public void loginFragmentButtonClicked(boolean success) {
        if (success) {
            logged_in = true;
            updateNavigationView();
            Toast.makeText(this, "Sikeres bejelentkezés", Toast.LENGTH_LONG).show();
            changeFragment(browseFragment);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_menu_login:
                changeFragment(loginFragment);
                break;
            case R.id.drawer_menu_prev_orders:
                PrevOrdersAlertDialog dialog = new PrevOrdersAlertDialog(this);
                dialog.show();
                break;
            case R.id.drawer_menu_logout:
                logged_in = false;
                updateNavigationView();
                changeFragment(openScreenFragment);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}