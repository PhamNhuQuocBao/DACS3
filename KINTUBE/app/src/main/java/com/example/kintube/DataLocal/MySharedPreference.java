package com.example.kintube.DataLocal;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;

public class MySharedPreference {
    private static final String MY_SHARED_PREFERENCE = "My Shared Preference";
    private Context context;

    public MySharedPreference(Context context) {
        this.context = context;
    }

    public void putIdAccount(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getIdAccount(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void putNameAccount(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getNameAccount(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void putEmailAccount(String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getEmailAccount(String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFERENCE,
                Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }
}
