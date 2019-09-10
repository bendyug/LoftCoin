package com.dbendyug.loftcoin.main;

import androidx.collection.SparseArrayCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.AppComponent;
import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.converter.ConverterFragment;
import com.dbendyug.loftcoin.exchangerates.ExchangeRatesFragment;
import com.dbendyug.loftcoin.fcm.FcmChannel;
import com.dbendyug.loftcoin.util.Supplier;
import com.dbendyug.loftcoin.wallets.WalletsFragment;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface MainModule {

    @Provides
    @Reusable
    static SparseArrayCompat<Supplier<Fragment>> fragments() {
        SparseArrayCompat<Supplier<Fragment>> fragments = new SparseArrayCompat<>();
        fragments.put(R.id.wallets, () -> new WalletsFragment());
        fragments.put(R.id.exchange_rates, () -> new ExchangeRatesFragment());
        fragments.put(R.id.converter, () -> new ConverterFragment());
        return fragments;
    }

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel mainViewModel);

    @Provides
    static FcmChannel fcmChannel(FragmentActivity fragmentActivity) {
        return AppComponent.from(fragmentActivity.getApplicationContext())
                .fcmChannel();
    }
}
