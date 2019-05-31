package com.telmopina.solidariedadediaria.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class AppGlobals extends Application {

    public static final String KEY_ID = "id";
    private static Context sContext;
    public static final String KEY_LOGIN = "login";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_NAME = "name";

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
    }

    public static void loginState(boolean type) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putBoolean(KEY_LOGIN, type).apply();
    }

    public static boolean isLogin() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getBoolean(KEY_LOGIN, false);
    }

    public static SharedPreferences getPreferenceManager() {
        return getContext().getSharedPreferences("shared_prefs", MODE_PRIVATE);
    }


    public static void saveStringToSharedPreferences(String key, String value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static String getStringFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getString(key, "");
    }


    public static void saveMoneyToSharedPreferences(String key, float value) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().putFloat(key, value).apply();
    }

    public static float getMoneyFromSharedPreferences(String key) {
        SharedPreferences sharedPreferences = getPreferenceManager();
        return sharedPreferences.getFloat(key, 0.0f);
    }



    public static void clearSettings() {
        SharedPreferences sharedPreferences = getPreferenceManager();
        sharedPreferences.edit().clear().commit();
    }

    public static Context getContext() {
        return sContext;
    }
}
