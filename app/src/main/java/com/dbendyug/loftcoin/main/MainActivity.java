package com.dbendyug.loftcoin.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.converter.ConverterFragment;
import com.dbendyug.loftcoin.exchangerates.ExchangeRatesFragment;
import com.dbendyug.loftcoin.wallets.WalletsFragment;
import com.dbendyug.loftcoin.util.Supplier;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import javax.inject.Inject;


public class MainActivity extends AppCompatActivity {

    @Inject MainNavigator mainNavigator;

    @Inject ViewModelProvider.Factory viewModelProviderFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent.builder()
        .activity(this)
        .build()
        .inject(this);

        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        final MainViewModel mainViewModel = ViewModelProviders.of(this, viewModelProviderFactory)
                .get(MainViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            mainViewModel.submitSelectedId(menuItem.getItemId());
            return true;
        });

        mainViewModel.title().observe(this, title -> Objects.requireNonNull(getSupportActionBar())
                .setTitle(title));

        mainViewModel.selectedId().observe(this, id -> mainNavigator.replaceFragment(id));

        mainViewModel.selectedId().observe(this, itemId -> bottomNavigationView.setSelectedItemId(itemId));

    }
}
