package com.dbendyug.loftcoin.converter;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.AppComponent;
import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
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
public interface ConverterModule {

    @Provides
    @Reusable
    static AppComponent appComponent(Fragment fragment) {
        return AppComponent.from(fragment.requireContext());
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

    @Provides
    static RxScheduler rxScheduler(AppComponent appComponent) {
        return appComponent.rxScheduler();
    }

    @Provides
    static CoinsRepository coinsRepository(AppComponent appComponent) {
        return appComponent.coinsRepository();
    }

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ClassKey(ConverterViewModel.class)
    ViewModel converterViewModel(ConverterViewModel converterViewModel);
}
