package com.dbendyug.loftcoin;

import android.app.Application;
import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

import dagger.Module;
import dagger.Provides;

@Module
public interface AppModule {

    @Provides
    static Context context(Application application){
        return application.getApplicationContext();
    }

    @Provides
    static Locale locale(Context context){
        LocaleListCompat localeList = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
        return localeList.get(0);
    }

}
