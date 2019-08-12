package com.dbendyug.loftcoin.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.adapter.WelcomePagerAdapter;

import me.relex.circleindicator.CircleIndicator;

public class WelcomeActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CircleIndicator circleIndicator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        viewPager = findViewById(R.id.viewpager);
        viewPager.setAdapter(new WelcomePagerAdapter(getLayoutInflater()));

        circleIndicator = findViewById(R.id.dots_indicator);
        circleIndicator.setViewPager(viewPager);
    }
}
