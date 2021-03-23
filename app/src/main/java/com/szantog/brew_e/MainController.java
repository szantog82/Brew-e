package com.szantog.brew_e;

import android.app.ProgressDialog;
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
import androidx.fragment.app.FragmentTransaction;

public class MainController extends AppCompatActivity implements OpenScreenFragmentCallback, BrowseFragmentCallback, NavigationView.OnNavigationItemSelectedListener, LoginFragmentCallback, OrderMenuFragment.OrderMenuFragmentCallback {

    private SharedPreferencesHandler sharedPreferencesHandler;

    private ProgressDialog progressDialog;

    private int actualFragmentLevel = 0;
    private static final int OPENSCREENFRAGMENT_ID = 10;
    private static final int BROWSEFRAGMENT_ID = 11;
    private static final int BLOGFRAGMENT_ID = 12;
    private static final int LOGINFRAGMENT_ID = 13;
    private static final int ORDERMENUFRAGMENT_ID = 14;
    private static final int ORDERSENTFRAGMENT_ID = 15;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView navHeaderNameTextView;

    private static boolean logged_in = false;
    private User user;
    private List<CoffeeShop> coffeeShopList;
    private List<DrinkItem> drinkItems;
    private List<BlogItem> blogList;
    private List<DrinkItem> bucket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferencesHandler = new SharedPreferencesHandler(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        View navigationHeaderView = navigationView.getHeaderView(0);
        navHeaderNameTextView = navigationHeaderView.findViewById(R.id.drawer_header_user_name);
        navHeaderNameTextView.setText("Vendég");

        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Folyamatban....");
        progressDialog.setTitle("Adatok letöltése");
        progressDialog.show();

        ConnectionHandler.testConnection(this, success -> {
            if (success) {
                progressDialog.dismiss();
                user = sharedPreferencesHandler.getUserData();
                if (user.getLogin().length() > 1) {
                    logged_in = true;
                    updateNavigationView();
                    changeFragment(OPENSCREENFRAGMENT_ID);
                } else {
                    Toast.makeText(this, "App használata vendégként", Toast.LENGTH_LONG).show();
                }
            } else {
                progressDialog.dismiss();
                Toast.makeText(this, "Adatok letöltése sikertelen", Toast.LENGTH_LONG).show();
                logged_in = false;
                changeFragment(OPENSCREENFRAGMENT_ID);
            }
        });

        ConnectionHandler.downloadShopsForMap(this, new ConnectionDownloadShopsForMapCallback() {
            @Override
            public void connectionMapResult(List<CoffeeShop> coffeeShops) {
                MainController.this.coffeeShopList = coffeeShops;
            }
        });
    }

    private void changeFragment(int fragmentId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentId) {
            case OPENSCREENFRAGMENT_ID:
                actualFragmentLevel = 0;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new OpenScreenFragment(this, user));
                break;
            case LOGINFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new LoginFragment(this));
                break;
            case BROWSEFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new BrowseFragment(this, coffeeShopList));
                break;
            case BLOGFRAGMENT_ID:
                actualFragmentLevel = 2;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new BlogFragment(blogList));
                break;
            case ORDERMENUFRAGMENT_ID:
                actualFragmentLevel = 2;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new OrderMenuFragment(drinkItems, this));
                break;
            case ORDERSENTFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new OrderSentFragment(bucket));
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void updateNavigationView() {
        navigationView.getMenu().clear();
        if (logged_in) {
            navigationView.inflateMenu(R.menu.drawer_menu_logged_in);
            navHeaderNameTextView.setText(user.getFamily_name() + " " + user.getFirst_name());
        } else {
            navigationView.inflateMenu(R.menu.drawer_menu_not_logged_in);
            navHeaderNameTextView.setText("Vendég");
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        switch (actualFragmentLevel) {
            case 0:
                this.moveTaskToBack(true);
                break;
            case 1:
                changeFragment(OPENSCREENFRAGMENT_ID);
                break;
            case 2:
                changeFragment(BROWSEFRAGMENT_ID);
                break;
        }
    }

    @Override
    public void openScreenFragmentButtonClicked(int buttonId) {
        switch (buttonId) {
            case OpenScreenFragment.LOGIN_BUTTON_ID:
                changeFragment(LOGINFRAGMENT_ID);
                break;
            case OpenScreenFragment.BROWSE_BUTTON_ID:
                if (coffeeShopList != null) {
                    changeFragment(BROWSEFRAGMENT_ID);
                }
                break;
            case OpenScreenFragment.BLOG_BUTTON_ID:
                Toast.makeText(this, "Itt majd megnyílnak a blogok", Toast.LENGTH_SHORT).show();
                //changeFragment(BLOGFRAGMENT_ID);
                break;
        }
    }

    @Override
    public void browseFragmentButtonClicked(int buttonId, int shop_Id) {
        if (buttonId == BrowseFragment.MENU_BUTTON_ID) {
            progressDialog.show();
            ConnectionHandler.downloadMenu(this, shop_Id, new ConnectionDownloadMenu() {
                @Override
                public void connectionMenuResult(List<DrinkItem> drinkItems) {
                    progressDialog.dismiss();
                    if (drinkItems != null) {
                        MainController.this.drinkItems = drinkItems;
                        changeFragment(ORDERMENUFRAGMENT_ID);
                    } else {
                        Toast.makeText(MainController.this, "Letöltés sikertelen", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else if (buttonId == BrowseFragment.BLOG_BUTTON_ID) {
            progressDialog.show();
            ConnectionHandler.downloadBlogs(this, shop_Id, new ConnectionDownloadBlogs() {
                @Override
                public void connectionBlogsResult(List<BlogItem> blogList) {
                    progressDialog.dismiss();
                    if (blogList != null) {
                        MainController.this.blogList = blogList;
                        changeFragment(BLOGFRAGMENT_ID);
                    } else {
                        Toast.makeText(MainController.this, "Letöltés sikertelen", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    @Override
    public void loginFragmentButtonClicked(final String login, String password) {
        ConnectionHandler.loginUser(MainController.this, login, password, new ConnectionLoginCallback() {
            @Override
            public void connectionLoginResult(boolean success) {
                if (success) {
                    logged_in = true;
                    user = sharedPreferencesHandler.getUserData();
                    Toast.makeText(MainController.this, "Sikeres bejelentkezés!", Toast.LENGTH_LONG).show();
                    updateNavigationView();
                    changeFragment(OPENSCREENFRAGMENT_ID);
                } else {
                    Toast.makeText(MainController.this, "Sikertelen bejelentkezés!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void OrderMenuFragmentOrderSubmitted(List<DrinkItem> bucket) {
        this.bucket = bucket;
        ConnectionHandler.uploadOrder(MainController.this, bucket);
        changeFragment(ORDERSENTFRAGMENT_ID);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_menu_login:
                changeFragment(LOGINFRAGMENT_ID);
                break;
            case R.id.drawer_menu_prev_orders:
                PrevOrdersAlertDialog dialog = new PrevOrdersAlertDialog(this);
                dialog.show();
                break;
            case R.id.drawer_menu_logout:
                ConnectionHandler.logoutUser(MainController.this, new ConnectionLogoutCallback() {
                    @Override
                    public void connectionLogoutResult(boolean success) {
                        if (success) {
                            logged_in = false;
                            sharedPreferencesHandler.clearUserData();
                            user = null;
                            Toast.makeText(MainController.this, "Kijelentkezés sikeres", Toast.LENGTH_LONG).show();
                            updateNavigationView();
                            changeFragment(OPENSCREENFRAGMENT_ID);
                        }
                    }
                });
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}