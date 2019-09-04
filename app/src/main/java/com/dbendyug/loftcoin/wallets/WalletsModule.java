package com.dbendyug.loftcoin.wallets;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.AppComponent;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.WalletsRepository;
import com.dbendyug.loftcoin.main.MainViewModel;
import com.dbendyug.loftcoin.rx.RxScheduler;

import java.util.Locale;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface WalletsModule {

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ClassKey(WalletsViewModel.class)
    ViewModel walletsViewModel(WalletsViewModel walletsViewModel);

    @Provides
    @Reusable
    static AppComponent appComponent(Fragment fragment) {
        return AppComponent.from(fragment.requireContext());
    }

    @Provides
    static WalletsRepository walletsRepository(AppComponent appComponent) {
        return appComponent.walletsRepository();
    }

    @Provides
    static RxScheduler rxScheduler(AppComponent appComponent) {
        return appComponent.rxScheduler();
    }

    @Provides
    @Reusable
    static CurrenciesReposytory currenciesRepository(AppComponent appComponent) {
        return appComponent.currenciesRepository();
    }

    @Provides
    @Reusable
    static Locale locale(AppComponent appComponent) {
        return appComponent.locale().get();
    }

}
