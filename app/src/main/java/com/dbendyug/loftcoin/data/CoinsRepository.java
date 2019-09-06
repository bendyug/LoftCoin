package com.dbendyug.loftcoin.data;

import androidx.lifecycle.LiveData;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.util.Consumer;

import java.util.List;

import io.reactivex.Observable;

public interface CoinsRepository {

//    void listing(String convert, Consumer<List<Coin>> onSuccess, Consumer<Throwable> onError);
//
//    LiveData<List<CoinEntity>> listings();
//
//    void refresh(String convert,
//                 Runnable onSuccess,
//                 Consumer<Throwable> onError);

    Observable<List<CoinEntity>> listings(String convert);
}
