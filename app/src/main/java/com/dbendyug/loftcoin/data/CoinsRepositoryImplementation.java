package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.BuildConfig;
import com.dbendyug.loftcoin.util.Consumer;

import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class CoinsRepositoryImplementation implements CoinsRepository {

    private static volatile CoinsRepositoryImplementation coinsRepositoryImplementation;

    private CoinApi coinApi;

    static CoinsRepositoryImplementation get() {
        CoinsRepositoryImplementation instance = coinsRepositoryImplementation;
        if (instance == null){
            synchronized (CoinsRepositoryImplementation.class){
                instance = coinsRepositoryImplementation;
                if (instance == null){
                    instance = coinsRepositoryImplementation = new CoinsRepositoryImplementation();
                }
            }
        }
        return instance;
    }

    private CoinsRepositoryImplementation (){
        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        okHttpClient.addInterceptor(new CoinApi.KeyInterceptor());
        if (BuildConfig.DEBUG){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.HEADERS);
            loggingInterceptor.redactHeader(CoinApi.KEY_HEADER);
            okHttpClient.addInterceptor(loggingInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder().client(okHttpClient.build())
                .baseUrl(BuildConfig.COIN_API_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        coinApi = retrofit.create(CoinApi.class);
    }

    @Override
    public void listing(String convert, Consumer<List<Coin>> onSuccess, Consumer<Throwable> onError) {
        coinApi.listings(convert).enqueue(new Callback<Listings>() {
            @Override
            public void onResponse(Call<Listings> call, Response<Listings> response) {
                Listings listings = response.body();
                if (listings != null && listings.data != null){
                    onSuccess.apply(Collections.unmodifiableList(listings.data));
                } else {
                    onSuccess.apply(Collections.emptyList());
                }
            }

            @Override
            public void onFailure(Call<Listings> call, Throwable t) {
                onError.apply(t);
            }
        });

    }
}
