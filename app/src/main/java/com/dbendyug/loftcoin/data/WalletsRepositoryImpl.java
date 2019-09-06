package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.LoftDb;
import com.dbendyug.loftcoin.db.TransactionEntity;
import com.dbendyug.loftcoin.db.WalletEntity;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

class WalletsRepositoryImpl implements WalletsRepository {

    private LoftDb loftDb;

    @Inject
    WalletsRepositoryImpl(LoftDb loftDb) {
        this.loftDb = loftDb;
    }

    @Override
    public Observable<List<WalletEntity.View>> wallets() {
        return loftDb.coins().coinsCount()
                .flatMap(count -> loftDb.wallets().fetchAllWallets());
    }

    @Override
    public Observable<List<TransactionEntity.View>> transactions(long walletId) {
        return loftDb.wallets().fetchAllTransactions(walletId);
    }

    @Override
    public Single<CoinEntity> findCoin() {
        return loftDb.wallets().findCoin();
    }

    @Override
    public Single<Long> saveWallet(WalletEntity wallet) {
        return loftDb.wallets().insertWallet(wallet);
    }

    @Override
    public Completable saveTransactions(List<TransactionEntity> transactions) {
        return loftDb.wallets().insertTransaction(transactions);
    }
}
