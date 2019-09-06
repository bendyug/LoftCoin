package com.dbendyug.loftcoin.exchangerates;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.Currency;
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.rx.RxScheduler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;


public class ExchangeRatesViewModel extends ViewModel {

    private CoinsRepository coinsRepository;

    private Subject<Boolean> needToRefresh;

    private Observable<ExchangeRatesUiState> uiState;

    private CurrenciesReposytory currenciesReposytory;

    private RxScheduler scheduler;

    @Inject
    ExchangeRatesViewModel(CoinsRepository coinsRepository,
                           CurrenciesReposytory currenciesReposytory,
                           RxScheduler scheduler) {
        this.coinsRepository = coinsRepository;
        this.currenciesReposytory = currenciesReposytory;
        this.scheduler = scheduler;

        needToRefresh = BehaviorSubject.createDefault(true);

        uiState = needToRefresh
                .observeOn(scheduler.io())
                .flatMap(refresh -> currenciesReposytory.currentCurrency())
                .map(currency -> currency.name())
                .flatMap(currencyName -> coinsRepository
                    .listings(currencyName)
                    .map(ExchangeRatesUiState::success)
                    .onErrorReturn(ExchangeRatesUiState::error)
                    .startWith(ExchangeRatesUiState.loading()))
                .subscribeOn(scheduler.io());
    }

//    public void setCurrency(Currency currency) {
//        currenciesReposytory.setCurrentCurrency(currency);
//        refresh();
//    }

    void refresh() {
        needToRefresh.onNext(true);
    }

    Observable<ExchangeRatesUiState> uiState(){
        return this.uiState.observeOn(scheduler.main());
    }

//    LiveData<Boolean> isRefreshing() {
//        return isRefreshing;
//    }
//
//    LiveData<Throwable> error() {
//        return error;
//    }
//
//    LiveData<List<CoinEntity>> coinData() {
//        return coinData;
//    }

}
