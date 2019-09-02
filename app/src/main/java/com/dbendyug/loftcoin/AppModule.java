package com.dbendyug.loftcoin;

import android.app.Application;
import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;
import androidx.room.Room;

import com.dbendyug.loftcoin.db.LoftDb;

import java.util.Locale;

import javax.inject.Singleton;

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

    @Provides
    @Singleton
    static LoftDb loftDb(Context context){
        return Room.databaseBuilder(context, LoftDb.class, "loftDb").fallbackToDestructiveMigration().build();
    }

}
