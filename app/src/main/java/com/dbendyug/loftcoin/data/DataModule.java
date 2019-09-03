package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.BuildConfig;

import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public interface DataModule {

    @Provides
    @Singleton
    static OkHttpClient okHttpClient(){
    OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new CoinApi.KeyInterceptor());
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            loggingInterceptor.redactHeader(CoinApi.KEY_HEADER);
            okHttpClient.addInterceptor(loggingInterceptor);
        }
        return okHttpClient.build();
    }

    @Provides
    @Singleton
    static CoinApi coinApi(OkHttpClient okHttpClient) {
        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient)
                .baseUrl(BuildConfig.COIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .callbackExecutor(Executors.newFixedThreadPool(4))
                .build();
        return retrofit.create(CoinApi.class);
    }

    @Binds
    CoinsRepository coinsRepository(CoinsRepositoryImplementation coinsRepositoryImplementation);

    @Binds
    CurrenciesReposytory currenciesRepository(CurrenciesReposytoryImpl currenciesReposytoryImpl);
}
