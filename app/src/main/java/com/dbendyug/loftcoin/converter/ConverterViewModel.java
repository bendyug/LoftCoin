package com.dbendyug.loftcoin.converter;

import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.rx.RxScheduler;
import com.dbendyug.loftcoin.util.PriceFormatter;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

class ConverterViewModel extends ViewModel {

    private Subject<Integer> from = BehaviorSubject.createDefault(0);

    private Subject<Integer> to = BehaviorSubject.createDefault(1);

    private Subject<String> fromValue = BehaviorSubject.create();

    private Subject<String> toValue = BehaviorSubject.create();

    private Observable<List<CoinEntity>> topCoins;

    private Observable<CoinEntity> fromCoin;

    private Observable<CoinEntity> toCoin;

    private Observable<Double> factor;

    private CoinsRepository coinsRepository;
    private RxScheduler rxScheduler;
    private PriceFormatter priceFormatter;

    @Inject
    ConverterViewModel(CoinsRepository coinsRepository,
                       RxScheduler rxScheduler,
                       PriceFormatter priceFormatter) {

        this.coinsRepository = coinsRepository;
        this.rxScheduler = rxScheduler;
        this.priceFormatter = priceFormatter;

        topCoins = coinsRepository
                .top(10)
                .replay(1)
                .autoConnect()
                .subscribeOn(rxScheduler.io());

        fromCoin = topCoins
                .switchMap(coins -> from
                        .map(coins::get)
                )
                .replay(1)
                .autoConnect()
                .subscribeOn(rxScheduler.io());

        toCoin = topCoins
                .switchMap(coins -> to
                        .map(coins::get)
                )
                .replay(1)
                .autoConnect()
                .subscribeOn(rxScheduler.io());

        factor = fromCoin
                .switchMap(f -> toCoin
                        .map(t -> f.price() / t.price())
                )
                .replay(1)
                .autoConnect()
                .subscribeOn(rxScheduler.io());
    }

    Observable<List<CoinEntity>> topCoins() {
        return topCoins.observeOn(rxScheduler.main());
    }

    Observable<CoinEntity> fromCoin() {
        return fromCoin.observeOn(rxScheduler.main());
    }

    Observable<CoinEntity> toCoin() {
        return toCoin.observeOn(rxScheduler.main());
    }

    Observable<String> toValue() {
        return fromValue
                .compose(parseValue())
                .switchMap(value -> factor.map(factor -> value * factor))
                .compose(formatValue())
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }

    Observable<String> fromValue() {
        return toValue
                .compose(parseValue())
                .switchMap(value -> factor.map(factor -> value / factor))
                .compose(formatValue())
                .subscribeOn(rxScheduler.io())
                .observeOn(rxScheduler.main());
    }

    void changeFromCoin(int position) {
        from.onNext(position);
    }

    void changeToCoin(int position) {
        to.onNext(position);
    }

    void changeFromValue(String text) {
        fromValue.onNext(text);
    }

    void changeToValue(String text) {
        toValue.onNext(text);
    }

    private ObservableTransformer<String, Double> parseValue() {
        return upstream -> upstream
                .distinctUntilChanged()
                .map(value -> value.isEmpty() ? "0" : value)
                .map(value -> value.trim().replace(',', '.'))
                .map(value -> value.replaceAll("\\s+", ""))
                .map(Double::parseDouble);
    }

    private ObservableTransformer<Double, String> formatValue() {
        return upstream -> upstream.map(value -> {
            if (value > 0) {
                return priceFormatter.format(value, "");
            } else {
                return "";
            }
        });
    }
}
