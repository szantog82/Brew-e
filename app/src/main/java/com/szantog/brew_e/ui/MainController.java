
package com.szantog.brew_e.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.szantog.brew_e.R;
import com.szantog.brew_e.common.AppButtonIdCollection;
import com.szantog.brew_e.common.SharedPreferencesHandler;
import com.szantog.brew_e.data.entities.OrderedItem;
import com.szantog.brew_e.domain.DrinkItem;
import com.szantog.brew_e.domain.User;
import com.szantog.brew_e.ui.blog.BlogFragment;
import com.szantog.brew_e.ui.browse.BrowseFragment;
import com.szantog.brew_e.ui.login.LoginFragment;
import com.szantog.brew_e.ui.openscreen.OpenScreenFragment;
import com.szantog.brew_e.ui.ordermenu.OrderMenuFragment;
import com.szantog.brew_e.ui.ordersent.OrderSentFragment;
import com.szantog.brew_e.ui.preferences.PreferencesFragment;
import com.szantog.brew_e.ui.previousorders.PreviousOrdersFragment;
import com.szantog.brew_e.ui.register.RegisterFragment;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class MainController extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String CHANNEL_ID = "2013435";

    private MainViewModel mainViewModel;
    private SendOrderViewModel sendOrderViewModel;

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
    private static final int PREFERENCESFRAGMENT_ID = 18;

    private Boolean isAllBlogs = false;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView navHeaderNameTextView;

    public int selectedShopId = -1;

    private static boolean logged_in = false;
    private User user;

    private FirebaseAuth mAuth;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public LiveData<User> getUser() {
        return mainViewModel.getUser();
    }

    public void setSelectedShopId(int shop_id) {
        this.selectedShopId = shop_id;
    }

    public int getSelectedShopId() {
        return this.selectedShopId;
    }

    public void showProgressDialog(boolean show) {
        if (show) {
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }
    }

    public void loginUser(String login, String password) {
        mainViewModel.loginUser(login, password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferencesHandler = new SharedPreferencesHandler(this);
        mAuth = FirebaseAuth.getInstance();

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mainViewModel.testConnection(sharedPreferencesHandler.getSessionId());

        mainViewModel.getClickedButtonId().observe(this, getClickedButtonIdObserver);
        mainViewModel.getBucketForOrder().observe(this, getBucketForOrdersObserver);
        mainViewModel.getUser().observe(this, getUserObserver);
        mainViewModel.getSessionId().observe(this, getSessionIdObserver);
        mainViewModel.getLoginToken().observe(this, getLoginTokenObserver);
        changeFragment(OPENSCREENFRAGMENT_ID);

        sendOrderViewModel = new ViewModelProvider(this).get(SendOrderViewModel.class);
        sendOrderViewModel.isUploadSuccess().observe(this, isUploadSuccessObserver);

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
    }

    private Observer getClickedButtonIdObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            int integer = (int) o;
            switch (integer) {
                case AppButtonIdCollection.LOAD_OPENSCREENFRAGMENT_ID:
                    changeFragment(OPENSCREENFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.OPENSCREENFRAGMENT_LOGIN_BUTTON_ID:
                    changeFragment(LOGINFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.OPENSCREENFRAGMENT_BROWSE_BUTTON_ID:
                    changeFragment(BROWSEFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.OPENSCREENFRAGMENT_BLOG_BUTTON_ID:
                    isAllBlogs = true;
                    changeFragment(BLOGFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.BROWSEFRAGMENT_MENU_BUTTON_ID:
                    changeFragment(ORDERMENUFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.BROWSEFRAGMENT_BLOG_BUTTON_ID:
                    changeFragment(BLOGFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.LOGINFRAGMENT_REGISTER_BUTTON_ID:
                    changeFragment(REGISTERFRAGMENT_ID);
                    break;
                case AppButtonIdCollection.PREFERENCESFRAGMENT_SAVE_BUTTON_ID:
                    changeFragment(OPENSCREENFRAGMENT_ID);
                    break;
            }
        }
    };

    private Observer getBucketForOrdersObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            List<DrinkItem> drinkItems = (List<DrinkItem>) o;
            if (drinkItems != null) {
                if (drinkItems.size() > 0) {
                    sendOrderViewModel.uploadOrder(sharedPreferencesHandler.getSessionId(), drinkItems);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy. MM. dd.");
                    for (DrinkItem drinkItem : drinkItems) {
                        OrderedItem orderedItem = new OrderedItem();
                        orderedItem.name = drinkItem.getItem_name();
                        orderedItem.shop_name = drinkItem.getShop_name();
                        orderedItem.date = simpleDateFormat.format(new Date(System.currentTimeMillis()));
                        orderedItem.price = drinkItem.getItem_price();
                        mainViewModel.insertOrderedItem(orderedItem);
                    }
                }
            }
        }
    };

    private Observer getUserObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            User user = (User) o;
            if (user == null || user.getLogin() == null || user.getLogin().length() < 1) {
                sharedPreferencesHandler.clearUserData();
                logged_in = false;
                Toast.makeText(MainController.this, "Használat vendégként", Toast.LENGTH_LONG).show();
            } else {
                logged_in = true;
                MainController.this.user = user;
                sharedPreferencesHandler.setUserData(user);
            }
            updateNavigationView();
            changeFragment(OPENSCREENFRAGMENT_ID);
        }
    };

    private Observer getSessionIdObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            String sessionId = (String) o;
            sharedPreferencesHandler.setSessionId(sessionId);
        }
    };

    private Observer getLoginTokenObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            String loginToken = (String) o;
            if (loginToken != null && loginToken.length() > 1) {
                mAuth.signInWithCustomToken(loginToken).addOnCompleteListener(MainController.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(MainController.this, new OnCompleteListener<String>() {
                                @Override
                                public void onComplete(@NonNull Task<String> task) {
                                    String firebaseToken = task.getResult();
                                    mainViewModel.uploadFirebaseToken(sharedPreferencesHandler.getSessionId(), firebaseToken);
                                }
                            });
                            //mAuth.signOut();
                        } else {
                            Toast.makeText(MainController.this, "Firebase bejelentkezés sikertelen", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    };

    private Observer isUploadSuccessObserver = new Observer() {
        @Override
        public void onChanged(Object o) {
            Boolean success = (Boolean) o;
            if (success) {
                Toast.makeText(MainController.this, "Rendelés sikeresen elküldve", Toast.LENGTH_LONG).show();
                changeFragment(ORDERSENTFRAGMENT_ID);
            } else {
                Toast.makeText(MainController.this, "Rendelés elküldése nem sikerült!", Toast.LENGTH_LONG).show();
            }
        }
    };


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
                    fragmentTransaction.replace(R.id.main_fragment_placeholder, new PreviousOrdersFragment());
                break;
            case PREFERENCESFRAGMENT_ID:
                actualFragmentLevel = 1;
                fragmentTransaction.replace(R.id.main_fragment_placeholder, new PreferencesFragment());
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
            case R.id.drawer_menu_preferences:
                changeFragment(PREFERENCESFRAGMENT_ID);
                break;
            case R.id.drawer_menu_logout:
                mainViewModel.logoutUser(sharedPreferencesHandler.getSessionId());
                logged_in = false;
                sharedPreferencesHandler.clearUserData();
                updateNavigationView();
                changeFragment(OPENSCREENFRAGMENT_ID);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}