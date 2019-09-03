package com.dbendyug.loftcoin.exchangerates;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.AppComponent;
import com.dbendyug.loftcoin.data.Coin;
import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.main.MainViewModel;
import com.dbendyug.loftcoin.rx.RxScheduler;
import com.dbendyug.loftcoin.util.Function;

import java.util.List;
import java.util.Locale;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.ClassKey;
import dagger.multibindings.IntoMap;

@Module
interface ExchangeRatesModule {

    @Binds
    @IntoMap
    @ClassKey(MainViewModel.class)
    ViewModel mainViewModel(MainViewModel mainViewModel);

    @Binds
    @IntoMap
    @ClassKey(ExchangeRatesViewModel.class)
    ViewModel exchangeRatesViewModel(ExchangeRatesViewModel exchangeRatesViewModel);

    @Provides
    @Reusable
    static AppComponent appComponent(Fragment fragment){
        return AppComponent.from(fragment.requireContext());
    }

    @Provides
    static CoinsRepository coinsRepository(AppComponent appComponent){
        return appComponent.coinsRepository();
    }

    @Provides
    static RxScheduler rxScheduler(AppComponent appComponent){
        return appComponent.rxScheduler();
    }

    @Provides
    @Reusable
    static CurrenciesReposytory currenciesRepository(AppComponent appComponent){
        return appComponent.currenciesRepository();
    }


    @Provides
    @Reusable
    static Locale locale (AppComponent appComponent){
        return appComponent.locale().get();
    }

    @Binds
    Function<List<Coin>, List<CoinExchangeRate>> exchangeRatesMapper(ExchangeRatesMapper exchangeRatesMapper);
}
