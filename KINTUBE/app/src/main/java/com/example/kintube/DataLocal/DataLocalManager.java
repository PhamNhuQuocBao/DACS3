package com.example.kintube.DataLocal;

import android.content.Context;

import java.util.Set;

public class DataLocalManager {
    private static final String ID_ACCOUNT_LOGIN = "ID_ACCOUNT_LOGIN";
    private static final String NAME_ACCOUNT_LOGIN = "NAME_ACCOUNT_LOGIN";
    private static final String EMAIL_ACCOUNT_LOGIN = "EMAIL_ACCOUNT_LOGIN";
    private static DataLocalManager instance;
    private MySharedPreference mySharedPreference;

    public static void init(Context context) {
        instance = new DataLocalManager();
        instance.mySharedPreference = new MySharedPreference(context);
    }

    public static DataLocalManager getInstance() {
        if (instance == null) {
            instance = new DataLocalManager();
        }

        return instance;
    }

    public static void setIdAccountLogin(String id) {
        DataLocalManager.getInstance().mySharedPreference.putIdAccount(ID_ACCOUNT_LOGIN, id);
    }

    public static String getIdAccountLogin() {
        return DataLocalManager.getInstance().mySharedPreference.getIdAccount(ID_ACCOUNT_LOGIN);
    }

    public static void setNameAccountLogin(String name) {
        DataLocalManager.getInstance().mySharedPreference.putNameAccount(NAME_ACCOUNT_LOGIN, name);
    }

    public static String getNameAccountLogin() {
        return DataLocalManager.getInstance().mySharedPreference.getNameAccount(NAME_ACCOUNT_LOGIN);
    }

    public static void setEmailAccountLogin(String email) {
        DataLocalManager.getInstance().mySharedPreference.putEmailAccount(EMAIL_ACCOUNT_LOGIN, email);
    }

    public static String getEmailAccountLogin() {
        return DataLocalManager.getInstance().mySharedPreference.getEmailAccount(EMAIL_ACCOUNT_LOGIN);
    }

}
