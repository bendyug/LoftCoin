package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.BuildConfig;
import com.dbendyug.loftcoin.util.Consumer;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
class CoinsRepositoryImplementation implements CoinsRepository {

    private CoinApi coinApi;

    @Inject CoinsRepositoryImplementation(CoinApi coinApi){
        this.coinApi = coinApi;
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
