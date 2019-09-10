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
import com.dbendyug.loftcoin.fcm.FcmChannel;
import com.dbendyug.loftcoin.wallets.WalletsFragment;
import com.dbendyug.loftcoin.util.Supplier;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import timber.log.Timber;


public class MainActivity extends AppCompatActivity {

    @Inject
    MainNavigator mainNavigator;

    @Inject
    ViewModelProvider.Factory viewModelProviderFactory;

    @Inject
    FcmChannel fcmChannel;

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

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

        compositeDisposable.add(fcmChannel.token().subscribe(token -> Timber.d(token)));

    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}
