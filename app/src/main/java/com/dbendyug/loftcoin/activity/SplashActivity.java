package com.dbendyug.loftcoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dbendyug.loftcoin.R;

public class SplashActivity extends AppCompatActivity {

    public static final String SHOW_WELCOME_SCREEN_KEY = "show_welcome_screen";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        new Handler().postDelayed(() -> {
            if (sharedPreferences.getBoolean(SHOW_WELCOME_SCREEN_KEY, true)) {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
        }, 2000);
    }
}
