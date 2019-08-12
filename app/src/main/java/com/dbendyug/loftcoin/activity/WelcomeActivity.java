package com.dbendyug.loftcoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.adapter.WelcomePagerAdapter;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private Button buttonStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new WelcomePagerAdapter(getLayoutInflater()));

        circleIndicator = findViewById(R.id.dots_indicator);
        circleIndicator.setViewPager(viewPager);

        buttonStart = findViewById(R.id.button_start);
        buttonStart.setOnClickListener(view -> {
            final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            sharedPreferences.edit().putBoolean(SplashActivity.SHOW_WELCOME_SCREEN_KEY, false).apply();
            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
