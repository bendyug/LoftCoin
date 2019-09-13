package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.db.CoinEntity;

import java.util.List;

import io.reactivex.Observable;

public interface CoinsRepository {

    Observable<List<CoinEntity>> listings(String convert, String sort);

    Observable<List<CoinEntity>> top(int limit);
}
