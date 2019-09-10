package com.dbendyug.loftcoin.wallets;

import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.data.WalletsRepository;
import com.dbendyug.loftcoin.db.Transaction;
import com.dbendyug.loftcoin.db.Wallet;
import com.dbendyug.loftcoin.rx.RxScheduler;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class WalletsViewModel extends ViewModel {

    private WalletsRepository walletsRepository;
    private RxScheduler rxScheduler;
    private Random random = new Random();

    private Observable<List<Wallet>> wallets;
    private Subject<Integer> walletPosition;
    private Observable<List<Transaction>> transactions;

    private Date date;

    @Inject
    WalletsViewModel(WalletsRepository walletsRepository,
                     RxScheduler rxScheduler) {

        this.walletsRepository = walletsRepository;
        this.rxScheduler = rxScheduler;

        wallets = walletsRepository.wallets()
                .replay(1)
                .autoConnect()
                .subscribeOn(rxScheduler.io());

        walletPosition = BehaviorSubject.createDefault(0);

        transactions = wallets.filter(wallets1 -> !wallets1.isEmpty())
                .switchMap(wallets -> walletPosition
                        .observeOn(rxScheduler.io())
                        .map(position -> Math.min(position, wallets.size() - 1))
                        .map(wallets::get)
                )
                .distinctUntilChanged(Wallet::id)
                .switchMap(walletsRepository::transactions)
                .subscribeOn(rxScheduler.io());

        date = new Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24 + random.nextInt(240)));
    }

    Completable createWallet() {
        return wallets
                .firstElement()
                .flatMapSingle(wallets -> Observable
                        .fromIterable(wallets)
                        .map(wallet -> wallet.coinEntity().id())
                        .toList()
                )
                .flatMap(walletsRepository::findCoin)
                .map(coin -> Wallet.create(
                        UUID.randomUUID().toString(),
                        random.nextDouble() * (1 + random.nextInt(25)),
                        coin
                ))
                .flatMapCompletable(walletsRepository::saveWallet)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }

    Completable createTransaction() {
        return wallets.filter(wallets1 -> !wallets1.isEmpty())
                .switchMap(wallets -> walletPosition
                        .map(position -> Math.min(position, wallets.size() - 1))
                        .map(wallets::get)
                )
                .distinctUntilChanged(Wallet::id)
                .map(wallet -> Transaction.create(UUID.randomUUID().toString(),
                        random.nextDouble() * (random.nextInt(30) - 15),
                        date,
                        wallet)
                )
                .flatMapCompletable(walletsRepository::saveTransaction)
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }


    void submitWalletPosition(int position) {
        walletPosition.onNext(position);
    }

    Observable<List<Wallet>> wallets() {
        return wallets.observeOn(rxScheduler.main());
    }

    Observable<List<Transaction>> transactions() {
        return transactions.observeOn(rxScheduler.main());
    }
}
