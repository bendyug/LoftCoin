package com.dbendyug.loftcoin.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.adapter.WelcomePagerAdapter;

import me.relex.circleindicator.CircleIndicator2;

public class WelcomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button buttonStart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setAdapter(new WelcomePagerAdapter(getLayoutInflater()));
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        CircleIndicator2 indicator = findViewById(R.id.dots_indicator);
        indicator.attachToRecyclerView(recyclerView, pagerSnapHelper);

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
