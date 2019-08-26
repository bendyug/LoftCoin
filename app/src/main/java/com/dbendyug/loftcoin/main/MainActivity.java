package com.dbendyug.loftcoin.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.converter.ConverterFragment;
import com.dbendyug.loftcoin.exchangerates.ExchangeRatesFragment;
import com.dbendyug.loftcoin.wallets.WalletsFragment;
import com.dbendyug.loftcoin.util.Supplier;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    private static final SparseArrayCompat<Supplier<Fragment>> FRAGMENTS;

    static {
        FRAGMENTS = new SparseArrayCompat<>();
        FRAGMENTS.put(R.id.wallets, () -> new WalletsFragment());
        FRAGMENTS.put(R.id.exchange_rates, () -> new ExchangeRatesFragment());
        FRAGMENTS.put(R.id.converter, () -> new ConverterFragment());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.toolbar));

        final MainViewModel mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(menuItem -> {
            mainViewModel.submitSelectedId(menuItem.getItemId());
            return true;
        });

        mainViewModel.title().observe(this, title -> Objects.requireNonNull(getSupportActionBar())
                .setTitle(title));

        mainViewModel.selectedId().observe(this, itemId -> replaceFragment(itemId));

        mainViewModel.selectedId().observe(this, itemId -> bottomNavigationView.setSelectedItemId(itemId));

    }
    private void replaceFragment(int itemId) {
        final Supplier<Fragment> factory = FRAGMENTS.get(itemId);
        if (factory != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_host, factory.get())
                    .commit();
        }
    }
}
