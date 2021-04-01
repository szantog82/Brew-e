package com.szantog.brew_e;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainController extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_ID = "2013435";

    private MainViewModel mainViewModel;
    private RetrofitListViewModel retrofitListViewModel;
    private DatabaseViewModel databaseViewModel;

    private SharedPreferencesHandler sharedPreferencesHandler;

    private ProgressDialog progressDialog;

    private int actualFragmentLevel = 0;
    private static final int OPENSCREENFRAGMENT_ID = 10;
    private static final int BROWSEFRAGMENT_ID = 11;
    private static final int BLOGFRAGMENT_ID = 12;
    private static final int LOGINFRAGMENT_ID = 13;
    private static final int ORDERMENUFRAGMENT_ID = 14;
    private static final int ORDERSENTFRAGMENT_ID = 15;
    private static final int REGISTERFRAGMENT_ID = 16;
    private static final int PREVIOUSORDERSFRAGMENT_ID = 17;

    private Intent serviceIntent;

    private Boolean isAllBlogs = false;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView navHeaderNameTextView;

    private static boolean logged_in = false;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        serviceIntent = new Intent(this, OrderService.class);

        sharedPreferencesHandler = new SharedPreferencesHandler(this);
        retrofitListViewModel = new ViewModelProvider(this).get(RetrofitListViewModel.class);
        databaseViewModel = new ViewModelProvider(this).get(DatabaseViewModel.class);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.getClickedButtonId().observe(this, integer -> {
            switch (integer) {
                case OpenScreenFragment.LOGIN_BUTTON_ID:
                    changeFragment(LOGINFRAGMENT_ID);
                    break;
                case OpenScreenFragment.BROWSE_BUTTON_ID:
                    changeFragment(BROWSEFRAGMENT_ID);
                    retrofitListViewModel.downloadShopList();
                    break;
                case OpenScreenFragment.BLOG_BUTTON_ID:
                    retrofitListViewModel.setSelectedShopId(-1);
                    retrofitListViewModel.downloadBlogs();
                    isAllBlogs = true;
                    changeFragment(BLOGFRAGMENT_ID);
                    break;
                case BrowseFragment.MENU_BUTTON_ID:
                    changeFragment(ORDERMENUFRAGMENT_ID);
                    retrofitListViewModel.downloadMenuItems();
                    break;
                case BrowseFragment.BLOG_BUTTON_ID:
                    changeFragment(BLOGFRAGMENT_ID);
                    retrofitListViewModel.downloadBlogs();
                    break;
                case LoginFragment.REGISTER_BUTTON_ID:
                    changeFragment(REGISTERFRAGMENT_ID);
                    break;
            }
        });
        changeFragment(OPENSCREENFRAGMENT_ID);
        mainViewModel.getBucketForOrder().observe(this, new Observer<List<DrinkItem>>() {
            @Override
            public void onChanged(List<DrinkItem> drinkItems) {
                if (drinkItems != null) {
                    if (drinkItems.size() > 0) {
                        retrofitListViewModel.uploadOrder(sharedPreferencesHandler.getSessionId(), drinkItems);
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd.");
                        for (DrinkItem drinkItem : drinkItems) {
                            OrderedItem orderedItem = new OrderedItem();
                            orderedItem.name = drinkItem.getItem_name();
                            orderedItem.shop_name = drinkItem.getShop_name();
                            orderedItem.date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                            orderedItem.price = drinkItem.getItem_price();
                            databaseViewModel.insertOrderedItem(orderedItem);
                        }
                    }
                }
            }
        });
        mainViewModel.getRegisterUserData().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                retrofitListViewModel.uploadUserRegistration(user);
            }
        });

        retrofitListViewModel.testConnection(sharedPreferencesHandler.getSessionId());
        retrofitListViewModel.getUser().observe(this, user -> {
            if (user == null || user.getLogin() == null || user.getLogin().length() < 1) {
                sharedPreferencesHandler.clearUserData();
                logged_in = false;
            } else {
                logged_in = true;
                startService(serviceIntent);
                MainController.this.user = user;
                sharedPreferencesHandler.setUserData(user);
            }
            updateNavigationView();
            changeFragment(OPENSCREENFRAGMENT_ID);
        });

        retrofitListViewModel.getSessionId().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                sharedPreferencesHandler.setSessionId(s);
            }
        });

        retrofitListViewModel.isUploadSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(MainController.this, "Rendelés sikeresen elküldve", Toast.LENGTH_LONG).show();
                    changeFragment(ORDERSENTFRAGMENT_ID);
                } else {
                    Toast.makeText(MainController.this, "Rendelés elküldése nem sikerült!", Toast.LENGTH_LONG).show();
                }
            }
        });

        retrofitListViewModel.getRegistrationresponse().observe(this, new Observer<ResponseModel>() {
            @Override
            public void onChanged(ResponseModel responseModel) {
                if (responseModel.getSuccess() == 1) {
                    sharedPreferencesHandler.setSessionId(responseModel.getSession_id());
                    Toast.makeText(MainController.this, "Sikeres regisztráció", Toast.LENGTH_LONG).show();
                    retrofitListViewModel.testConnection(responseModel.getSession_id());
                    changeFragment(OPENSCREENFRAGMENT_ID);
                } else {
                    Toast.makeText(MainController.this, "Sikertelen regisztráció!", Toast.LENGTH_LONG).show();
                }
            }
        });


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

        retrofitListViewModel.isDownloading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    progressDialog.show();
                } else {
                    progressDialog.dismiss();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Brew-e", NotificationManager.IMPORTANCE_HIGH);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    private void changeFragment(int fragmentId) {
        boolean fragmentPopped = false;
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FragmentManager manager = getSupportFragmentManager();
        switch (fragmentId) {
            case OPENSCREENFRAGMENT_ID:
                actualFragmentLevel = 0;
                manager = getSupportFragmentManager();
                fragmentPopped = manager.popBackStackImmediate(String.valueOf(OPENSCREENFRAGMENT_ID), 0);
                if (!fragmentPopped) {
                    fragmentTransaction.replace(R.id.main_fragment_placeholder, new OpenScreenFragment());
                }
                break;
            case LOGINFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new LoginFragment());
                break;
            case BROWSEFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentPopped = manager.popBackStackImmediate(String.valueOf(BROWSEFRAGMENT_ID), 0);
                if (!fragmentPopped)
                    fragmentTransaction.replace(R.id.main_fragment_placeholder, new BrowseFragment());
                break;
            case BLOGFRAGMENT_ID:
                if (isAllBlogs) {
                    actualFragmentLevel = 1;
                    isAllBlogs = false;
                } else {
                    actualFragmentLevel = 2;
                }
                fragmentPopped = manager.popBackStackImmediate(String.valueOf(BLOGFRAGMENT_ID), 0);
                if (!fragmentPopped) {
                    fragmentTransaction.replace(R.id.main_fragment_placeholder, new BlogFragment());
                }
                break;
            case ORDERMENUFRAGMENT_ID:
                actualFragmentLevel = 2;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new OrderMenuFragment());
                break;
            case ORDERSENTFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new OrderSentFragment());
                break;
            case REGISTERFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new RegisterFragment());
                break;
            case PREVIOUSORDERSFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentPopped = manager.popBackStackImmediate(String.valueOf(PREVIOUSORDERSFRAGMENT_ID), 0);
                if (!fragmentPopped)
                    fragmentTransaction.replace(R.id.main_fragment_placeholder, new PreviousOrdersFragments());
                break;
        }
        if (!fragmentPopped) {
            fragmentTransaction.addToBackStack(String.valueOf(fragmentId));
            fragmentTransaction.commit();
        }
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
        navigationView.setNavigationItemSelectedListener(this);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.drawer_menu_login:
                changeFragment(LOGINFRAGMENT_ID);
                break;
            case R.id.drawer_menu_prev_orders:
                changeFragment(PREVIOUSORDERSFRAGMENT_ID);
                break;
            case R.id.drawer_menu_logout:
                retrofitListViewModel.logoutUser(sharedPreferencesHandler.getSessionId());
                stopService(serviceIntent);
                logged_in = false;
                sharedPreferencesHandler.clearUserData();
                updateNavigationView();
                changeFragment(OPENSCREENFRAGMENT_ID);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }
}