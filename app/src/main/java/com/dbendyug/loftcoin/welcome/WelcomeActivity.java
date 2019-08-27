package com.dbendyug.loftcoin.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.main.MainActivity;
import com.dbendyug.loftcoin.util.Settings;

import me.relex.circleindicator.CircleIndicator2;

public class WelcomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView buttonStart;

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

            Settings settings = Settings.of(this);
            settings.doNotShowWelcomeScreenAgain();

            Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }
}
