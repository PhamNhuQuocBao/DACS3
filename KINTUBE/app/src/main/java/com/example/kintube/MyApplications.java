package com.example.kintube;

import android.app.Application;

import com.example.kintube.DataLocal.DataLocalManager;

public class MyApplications extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataLocalManager.init(getApplicationContext());
    }
}
