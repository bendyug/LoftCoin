package com.dbendyug.loftcoin.data;

import androidx.lifecycle.LiveData;

import com.dbendyug.loftcoin.BuildConfig;
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.LoftDb;
import com.dbendyug.loftcoin.rx.RxScheduler;
import com.dbendyug.loftcoin.util.Consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
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
    private LoftDb loftDb;
    private RxScheduler rxScheduler;

    @Inject
    CoinsRepositoryImplementation(CoinApi coinApi,
                                  LoftDb loftDb,
                                  RxScheduler rxScheduler) {
        this.coinApi = coinApi;
        this.loftDb = loftDb;
        this.rxScheduler = rxScheduler;
    }

    @Override
    public Observable<List<CoinEntity>> listings(String convert) {
        return Observable.merge(loftDb.coins().fetchAll(),
                coinApi.listings(convert)
                        .map(listings -> convertToEntity(listings))
                        .doOnNext(coins -> loftDb.coins().insertAll(coins))
                        .skip(1)
                        .subscribeOn(rxScheduler.io()));
    }

    private List<CoinEntity> convertToEntity(Listings listings) {
        if (listings != null && listings.data != null) {
            List<CoinEntity> entities = new ArrayList<>();
            for (Coin coin : listings.data){
                double price = 0d;
                double change24h = 0d;
                Iterator<Quote> quoteIterator = coin.getQuote().values().iterator();
                if (quoteIterator.hasNext()){
                    Quote quote = quoteIterator.next();
                    price = quote.getPrice();
                    change24h = quote.getChange24h();
                }
                entities.add(CoinEntity.create(coin.getId(), coin.getSymbol(), price, change24h));
            }
            return Collections.unmodifiableList(entities);
//            loftDb.coins().insertAll(entities);
        }
        return Collections.emptyList();
    }
}

//    @Override
//    public void listing(String convert, Consumer<List<Coin>> onSuccess, Consumer<Throwable> onError) {
//        coinApi.listings(convert).enqueue(new Callback<Listings>() {
//            @Override
//            public void onResponse(Call<Listings> call, Response<Listings> response) {
//                Listings listings = response.body();
//                if (listings != null && listings.data != null){
//                    onSuccess.apply(Collections.unmodifiableList(listings.data));
//                } else {
//                    onSuccess.apply(Collections.emptyList());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Listings> call, Throwable t) {
//                onError.apply(t);
//            }
//        });
//
//    }
//
//    @Override
//    public LiveData<List<CoinEntity>> listings() {
//        return loftDb.coins().fetchAll();
//    }
//
//    @Override
//    public void refresh(String convert, Runnable onSuccess, Consumer<Throwable> onError) {
//        listing(convert, coins -> {
//            List<CoinEntity> entities = new ArrayList<>();
//            for (Coin coin : coins){
//                double price = 0d;
//                double change24h = 0d;
//                Quote quote = coin.getQuote().get(convert);
//                if (quote != null){
//                    price = quote.getPrice();
//                    change24h = quote.getChange24h();
//                }
//                entities.add(CoinEntity.create(coin.getId(), coin.getSymbol(), price, change24h));
//            }
//            loftDb.coins().insertAll(entities);
//            onSuccess.run();
//        }, onError);
