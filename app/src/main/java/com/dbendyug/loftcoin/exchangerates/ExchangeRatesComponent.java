package com.dbendyug.loftcoin.exchangerates;

import androidx.fragment.app.Fragment;

import com.dbendyug.loftcoin.util.UtilModule;
import com.dbendyug.loftcoin.viewmodel.ViewModelModule;

import dagger.BindsInstance;
import dagger.Component;

@Component(modules = {
        ExchangeRatesModule.class,
        ViewModelModule.class,
        UtilModule.class
})
interface ExchangeRatesComponent {

    void inject(ExchangeRatesFragment exchangeRatesFragment);

    void inject(CurrencyDialog currencyDialog);

    @Component.Builder
    interface Builder{

        @BindsInstance
        Builder fragment(Fragment fragment);

        ExchangeRatesComponent build();
    }
}
