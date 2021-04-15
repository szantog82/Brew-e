package com.szantog.brew_e;

import android.content.Context;
import android.content.SharedPreferences;

import com.szantog.brew_e.model.User;

public class SharedPreferencesHandler {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor sharedEditor;

    private static final String SHARED_PREF_MAIN = "com.szantog82.brewe.shared_pref_main";
    private static final String LOGIN_SESSION_PREF = "com.szantog82.brewe.login_session_pref";
    private static final String LOGIN_ID_PREF = "com.szantog82.brewe.login_id_pref";
    private static final String LOGIN_LOGIN_PREF = "com.szantog82.brewe.login_login_pref";
    private static final String LOGIN_FIRST_NAME_PREF = "com.szantog82.brewe.login_first_name_pref";
    private static final String LOGIN_FAMILY_NAME_PREF = "com.szantog82.brewe.login_family_name_pref";
    private static final String LOGIN_EMAIL_PREF = "com.szantog82.brewe.login_email_pref";
    private static final String LOGIN_POSTALCODE_PREF = "com.szantog82.brewe.login_postalcode_pref";
    private static final String LOGIN_COUNTRY_PREF = "com.szantog82.brewe.login_country_pref";
    private static final String LOGIN_CITY_PREF = "com.szantog82.brewe.login_city_pref";
    private static final String LOGIN_STREET_PREF = "com.szantog82.brewe.login_street_pref";

    public SharedPreferencesHandler(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_MAIN, Context.MODE_PRIVATE);
        sharedEditor = sharedPreferences.edit();
    }

    public void setLoginData(String login, String session_id) {
        sharedEditor.putString(LOGIN_LOGIN_PREF, login);
        sharedEditor.putString(LOGIN_SESSION_PREF, session_id);
        sharedEditor.apply();
    }

    public void setSessionId(String session_id) {
        sharedEditor.putString(LOGIN_SESSION_PREF, session_id);
        sharedEditor.apply();
    }

    public String getSessionId() {
        return sharedPreferences.getString(LOGIN_SESSION_PREF, "");
    }

    public void setUserData(User user) {
        sharedEditor.putInt(LOGIN_ID_PREF, user.getId());
        sharedEditor.putString(LOGIN_LOGIN_PREF, user.getLogin());
        sharedEditor.putString(LOGIN_EMAIL_PREF, user.getEmail());
        sharedEditor.putString(LOGIN_FAMILY_NAME_PREF, user.getFamily_name());
        sharedEditor.putString(LOGIN_FIRST_NAME_PREF, user.getFirst_name());
        sharedEditor.putInt(LOGIN_POSTALCODE_PREF, user.getPostalcode());
        sharedEditor.putString(LOGIN_COUNTRY_PREF, user.getCountry());
        sharedEditor.putString(LOGIN_CITY_PREF, user.getCity());
        sharedEditor.putString(LOGIN_STREET_PREF, user.getStreet());
        sharedEditor.apply();
    }

    public User getUserData() {
        int id = sharedPreferences.getInt(LOGIN_ID_PREF, 0);
        String login = sharedPreferences.getString(LOGIN_LOGIN_PREF, "");
        String email = sharedPreferences.getString(LOGIN_EMAIL_PREF, "");
        String family_name = sharedPreferences.getString(LOGIN_FAMILY_NAME_PREF, "");
        String first_name = sharedPreferences.getString(LOGIN_FIRST_NAME_PREF, "");
        int postalcode = sharedPreferences.getInt(LOGIN_POSTALCODE_PREF, 0);
        String country = sharedPreferences.getString(LOGIN_COUNTRY_PREF, "");
        String city = sharedPreferences.getString(LOGIN_CITY_PREF, "");
        String street = sharedPreferences.getString(LOGIN_STREET_PREF, "");
        return new User(id, login, email, family_name, first_name, postalcode, country, city, street);
    }

    public void clearUserData() {
        sharedEditor.remove(LOGIN_SESSION_PREF);
        sharedEditor.remove(LOGIN_ID_PREF);
        sharedEditor.remove(LOGIN_LOGIN_PREF);
        sharedEditor.remove(LOGIN_EMAIL_PREF);
        sharedEditor.remove(LOGIN_FAMILY_NAME_PREF);
        sharedEditor.remove(LOGIN_FIRST_NAME_PREF);
        sharedEditor.remove(LOGIN_POSTALCODE_PREF);
        sharedEditor.remove(LOGIN_COUNTRY_PREF);
        sharedEditor.remove(LOGIN_CITY_PREF);
        sharedEditor.remove(LOGIN_STREET_PREF);
        sharedEditor.apply();
    }

}
