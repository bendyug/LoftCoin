package com.dbendyug.loftcoin;

import android.app.Application;
import android.content.Context;

import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.DataModule;

import java.util.Locale;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component (modules = {
        AppModule.class,
        DataModule.class
})
public interface AppComponent {

    static AppComponent from(Context context){
        return ((LoftApp) context.getApplicationContext()).getAppComponent();
    }

    Provider<Locale> locale();

    CoinsRepository coinsRepository();

    CurrenciesReposytory currenciesRepository();

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }
}