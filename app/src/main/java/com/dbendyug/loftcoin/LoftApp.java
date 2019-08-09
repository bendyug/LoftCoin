package com.dbendyug.loftcoin;

import android.app.Application;
import android.os.StrictMode;

import com.dbendyug.loftcoin.log.DebugTree;

import timber.log.Timber;

public class LoftApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG){
            StrictMode.enableDefaults();
            Timber.plant(new DebugTree());
        }
    }
}