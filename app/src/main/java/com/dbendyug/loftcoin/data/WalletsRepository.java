package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.TransactionEntity;
import com.dbendyug.loftcoin.db.WalletEntity;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface WalletsRepository {

    Observable<List<WalletEntity.View>> wallets();

    Observable<List<TransactionEntity.View>> transactions(long walletId);

    Single<CoinEntity> findCoin();

    Single<Long> saveWallet(WalletEntity wallet);

    Completable saveTransactions(List<TransactionEntity> transactions);
}
