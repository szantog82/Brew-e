package com.szantog.brew_e;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        if (previousFragment == browseFragment) {
            downloadAndSetBrowseFragmentData();
        }
        changeFragment(previousFragment);
    }

    private void downloadAndSetBrowseFragmentData() {
        Call<List<CoffeeShop>> call = RetrofitClient.getInstance().getCoffeeShops();
        call.enqueue(new Callback<List<CoffeeShop>>() {
            @Override
            public void onResponse(Call<List<CoffeeShop>> call, Response<List<CoffeeShop>> response) {
                List<CoffeeShop> coffeeShopList = response.body();
                browseFragment.addCoffeeShopsToMap(coffeeShopList);
            }

            @Override
            public void onFailure(Call<List<CoffeeShop>> call, Throwable t) {
                Toast.makeText(MainController.this, "Kávézók letöltése sikertelen!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void downloadAndSetOrderMenuData(int shop_id) {
        Call<List<DrinkMenu>> call = RetrofitClient.getInstance().getDrinkMenu(shop_id);
        call.enqueue(new Callback<List<DrinkMenu>>() {
            @Override
            public void onResponse(Call<List<DrinkMenu>> call, Response<List<DrinkMenu>> response) {
                List<DrinkMenu> drinkMenus = response.body();
                orderMenuFragment.addMenuItems(drinkMenus);
            }

            @Override
            public void onFailure(Call<List<DrinkMenu>> call, Throwable t) {

            }
        });
    }

    @Override
    public void openScreenFragmentButtonClicked(int buttonId) {
        previousFragment = openScreenFragment;
        switch (buttonId) {
            case OpenScreenFragment.LOGIN_BUTTON_ID:
                changeFragment(loginFragment);
                break;
            case OpenScreenFragment.BROWSE_BUTTON_ID:
                downloadAndSetBrowseFragmentData();
                changeFragment(browseFragment);
                break;
            case OpenScreenFragment.BLOG_BUTTON_ID:
                changeFragment(blogFragment);
                break;
        }
    }

    @Override
    public void browseFragmentButtonClicked(int buttonId, int shop_Id) {
        previousFragment = browseFragment;
        if (buttonId == BrowseFragment.MENU_BUTTON_ID) {
            downloadAndSetOrderMenuData(shop_Id);
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