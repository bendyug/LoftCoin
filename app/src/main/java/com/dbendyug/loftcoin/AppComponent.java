package com.dbendyug.loftcoin;

import android.app.Application;
import android.content.Context;

import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.DataModule;
import com.dbendyug.loftcoin.data.WalletsRepository;
import com.dbendyug.loftcoin.db.LoftDb;
import com.dbendyug.loftcoin.fcm.FcmChannel;
import com.dbendyug.loftcoin.fcm.FcmModule;
import com.dbendyug.loftcoin.fcm.FcmService;
import com.dbendyug.loftcoin.rx.RxModule;
import com.dbendyug.loftcoin.rx.RxScheduler;

import java.util.Locale;

import javax.inject.Provider;
import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        DataModule.class,
        RxModule.class,
        FcmModule.class
})
public interface AppComponent {

    static AppComponent from(Context context) {
        return ((LoftApp) context.getApplicationContext()).getAppComponent();
    }

    Provider<Locale> locale();

    CoinsRepository coinsRepository();

    CurrenciesReposytory currenciesRepository();

    RxScheduler rxScheduler();

    WalletsRepository walletsRepository();

    FcmChannel fcmChannel();

    LoftDb loftDb();

    void inject(FcmService fcmService);

    @Component.Factory
    interface Factory {
        AppComponent create(@BindsInstance Application application);
    }
}
