package com.dbendyug.loftcoin.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

@Dao
public interface WalletDao {

    @Query("SELECT * FROM wallets_view")
    Observable<List<WalletEntity.View>> fetchAllWallets();

    @Query("SELECT * FROM transactions_view WHERE wallet_id=:walletId ORDER BY timestamp DESC")
    Observable<List<TransactionEntity.View>> fetchAllTransactions(long walletId);

    @Query("SELECT * FROM coins WHERE id NOT IN (SELECT coin_id FROM wallets) LIMIT 1")
    Single<CoinEntity> findCoin();

    @Insert
    Single<Long> insertWallet(WalletEntity walletEntity);

    @Insert
    Completable insertTransaction(List<TransactionEntity> transactions);
}
