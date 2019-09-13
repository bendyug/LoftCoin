package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.db.LoftDb;
import com.dbendyug.loftcoin.db.Transaction;
import com.dbendyug.loftcoin.db.Wallet;
import com.dbendyug.loftcoin.rx.RxScheduler;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Single;

@Singleton
class WalletsRepositoryImpl implements WalletsRepository {

    private RxScheduler rxScheduler;

    private LoftDb loftDb;

    private FirebaseFirestore firestore;

    private Executor executor;

    @Inject
    WalletsRepositoryImpl(RxScheduler schedulers, LoftDb loftDb) {
        rxScheduler = schedulers;
        this.loftDb = loftDb;
        firestore = FirebaseFirestore.getInstance();
        executor = schedulers.io()::scheduleDirect;
    }

    @Override
    public Observable<List<Wallet>> wallets() {
        return Observable.create(new FirestoreOnSubscribe(executor, firestore
                .collection("wallets")
                .orderBy("created", Query.Direction.ASCENDING)))
                .flatMap(documents -> Observable
                        .fromIterable(documents)
                        .flatMapSingle(document -> loftDb.coins()
                                .fetchCoin(document.getLong("coinId"))
                                .map(coin -> createWallet(document, coin))
                        )
                        .toList().toObservable()
                );
    }

    @Override
    public Observable<List<Transaction>> transactions(Wallet wallet) {
        return Observable.create(new FirestoreOnSubscribe(executor, firestore
                .collection("wallets")
                .document(wallet.id())
                .collection("transactions")
                .orderBy("timestamp", Query.Direction.DESCENDING)))
                .flatMap(documents -> Observable.fromIterable(documents)
                        .map(document -> createTransaction(document, wallet))
                        .toList().toObservable()
                );
    }

    @Override
    public Single<CoinEntity> findCoin(List<Long> exclude) {
        return loftDb.coins().findCoin(exclude);
    }

    @Override
    public Completable saveWallet(Wallet wallet) {
        return Single.fromCallable(() -> {
            Map<String, Object> data = new HashMap<>();
            data.put("balance", wallet.coinBalance());
            data.put("coinId", wallet.coinEntity().id());
            data.put("symbol", wallet.coinEntity().symbol());
            data.put("created", FieldValue.serverTimestamp());
            return data;
        }).flatMapCompletable(data -> Completable.create(emitter -> {
            firestore
                    .collection("wallets")
                    .add(data)
                    .addOnCompleteListener(executor, doc -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(executor, emitter::tryOnError);
        })).subscribeOn(rxScheduler.io());
    }

    @Override
    public Completable saveTransaction(Transaction transaction) {
        return Single.fromCallable(() -> {
            Map<String, Object> data = new HashMap<>();
            data.put("amount", transaction.coinAmount());
            data.put("timestamp", transaction.timestamp());
            return data;
        }).flatMapCompletable(data -> Completable.create(emitter -> {
            firestore.collection("wallets")
                    .document(transaction.wallet().id())
                    .collection("transactions")
                    .add(data)
                    .addOnCompleteListener(executor, doc -> {
                        if (!emitter.isDisposed()) {
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(executor, emitter::tryOnError);
        })).subscribeOn(rxScheduler.io());
    }

    private Wallet createWallet(DocumentSnapshot snapshot,
                                CoinEntity coin) {
        return Wallet.create(snapshot.getId(), snapshot.getDouble("balance"), coin);
    }

    private Transaction createTransaction(DocumentSnapshot snapshot,
                                          Wallet wallet) {
        return Transaction.create(
                snapshot.getId(),
                snapshot.getDouble("amount"),
                snapshot.getDate("timestamp"),
                wallet
        );
    }

    private static class FirestoreOnSubscribe implements ObservableOnSubscribe<List<DocumentSnapshot>> {

        private Query query;

        private Executor executor;

        FirestoreOnSubscribe(Executor executor,
                             Query query) {
            this.executor = executor;
            this.query = query;
        }

        @Override
        public void subscribe(ObservableEmitter<List<DocumentSnapshot>> emitter) {
            final ListenerRegistration registration = query
                    .addSnapshotListener(executor, (snapshots, e) -> {
                        if (snapshots != null) {
                            if (!emitter.isDisposed()) {
                                emitter.onNext(snapshots.getDocuments());
                            }
                        } else if (e != null) {
                            emitter.tryOnError(e);
                        }
                    });
            emitter.setCancellable(registration::remove);
        }

    }

}
