package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.Transaction;
import com.dbendyug.loftcoin.db.Wallet;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface WalletsRepository {

    Observable<List<Wallet>> wallets();

    Observable<List<Transaction>> transactions(Wallet wallet);

    Single<CoinEntity> findCoin(List<Long> excludeId);

    Completable saveWallet(Wallet wallet);

    Completable saveTransaction(Transaction transaction);

}
