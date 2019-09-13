package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.LoftDb;
import com.dbendyug.loftcoin.rx.RxScheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

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
    public Observable<List<CoinEntity>> listings(String convert, String sort) {
        if (sort.equals("DESC")) {
            return Observable.merge(loftDb.coins().fetchAll(),
                    coinApi.listings(convert)
                            .map(listings -> convertToEntity(listings))
                            .doOnNext(coins -> loftDb.coins().insertAll(coins))
                            .skip(1)
                            .subscribeOn(rxScheduler.io()));
        } else if (sort.equals("ASC")) {
            return Observable.merge(loftDb.coins().fetchAllAsc(),
                    coinApi.listings(convert)
                            .map(listings -> convertToEntity(listings))
                            .doOnNext(coins -> loftDb.coins().insertAll(coins))
                            .skip(1)
                            .subscribeOn(rxScheduler.io()));
        }
        return null;
    }

    @Override
    public Observable<List<CoinEntity>> top(int limit) {
        return loftDb.coins().fetchCoins(limit);
    }

    private List<CoinEntity> convertToEntity(Listings listings) {
        if (listings != null && listings.data != null) {
            List<CoinEntity> entities = new ArrayList<>();
            for (Coin coin : listings.data) {
                double price = 0d;
                double change24h = 0d;
                Iterator<Quote> quoteIterator = coin.getQuote().values().iterator();
                if (quoteIterator.hasNext()) {
                    Quote quote = quoteIterator.next();
                    price = quote.getPrice();
                    change24h = quote.getChange24h();
                }
                entities.add(CoinEntity.create(coin.getId(), coin.getName(), coin.getSymbol(), price, change24h));
            }
            return Collections.unmodifiableList(entities);
        }
        return Collections.emptyList();
    }
}