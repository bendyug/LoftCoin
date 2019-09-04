package com.dbendyug.loftcoin.wallets;

import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.data.WalletsRepository;
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.TransactionEntity;
import com.dbendyug.loftcoin.db.WalletEntity;
import com.dbendyug.loftcoin.rx.RxScheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

class WalletsViewModel extends ViewModel {

    private WalletsRepository walletsRepository;
    private RxScheduler rxScheduler;
    private Random random = new Random();
    private BehaviorSubject<Long> walletId = BehaviorSubject.create();

    @Inject
    WalletsViewModel(WalletsRepository walletsRepository,
                     RxScheduler rxScheduler) {

        this.walletsRepository = walletsRepository;
        this.rxScheduler = rxScheduler;
    }

    Completable creatWallet() {
        return walletsRepository.findCoin()
                .map(this::createWallet)
                .flatMap(wallet -> walletsRepository.saveWallet(wallet))
                .map(this::createTransactions)
                .flatMapCompletable(transactions -> walletsRepository.saveTransactions(transactions))
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }

    Observable<List<WalletEntity.View>> wallets() {
        return walletsRepository.wallets().doOnNext(wallets -> {
            Long value = walletId.getValue();
            if (value == null && !wallets.isEmpty()) {
                walletId.onNext(wallets.get(0).id());
            }
        })
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }

    Observable<List<TransactionEntity.View>> transactions() {
        return walletId.distinctUntilChanged()
                .flatMap(walletId -> walletsRepository.transactions(walletId))
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }

    void sumbitWalletId(long id) {
        walletId.onNext(id);
    }

    private WalletEntity createWallet(CoinEntity coinEntity) {
        return WalletEntity.create(0,
                random.nextDouble() * 100,
                coinEntity.id());
    }

    private List<TransactionEntity> createTransactions(long walletId) {
        int numberOfTransactions = 1 + random.nextInt(15);
        List<TransactionEntity> transactions = new ArrayList<>(numberOfTransactions);
        long now = System.currentTimeMillis();
        for (int i = 0; i < numberOfTransactions; i++) {
            transactions.add(TransactionEntity.create(0,
                    now - TimeUnit.HOURS.toMillis(24 + random.nextInt(240)),
                    random.nextDouble() * (random.nextInt(20) - 10),
                    walletId));
        }
        return transactions;
    }

}
