package com.dbendyug.loftcoin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.util.Settings;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Settings settings = Settings.of(this);

        new Handler().postDelayed(() -> {
            if (settings.showWelcomeScreen()) {
                startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
        }, 2000);
    }
}
