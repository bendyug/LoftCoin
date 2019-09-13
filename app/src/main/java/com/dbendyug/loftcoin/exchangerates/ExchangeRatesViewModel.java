package com.dbendyug.loftcoin.exchangerates;

import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.db.LoftDb;
import com.dbendyug.loftcoin.rx.RxScheduler;

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
    private static String sort = "DESC";
    @Inject
    LoftDb loftDb;

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
                        .listings(currencyName, sort)
                        .map(ExchangeRatesUiState::success)
                        .onErrorReturn(ExchangeRatesUiState::error)
                        .startWith(ExchangeRatesUiState.loading()))
                .subscribeOn(scheduler.io());
    }

    void refresh() {
        needToRefresh.onNext(true);
    }

    void sortItems() {
        if (sort.equals("DESC")) {
            sort = "ASC";
            refresh();
        } else if (sort.equals("ASC")) {
            sort = "DESC";
            refresh();
        }
    }

    Observable<ExchangeRatesUiState> uiState() {
        return this.uiState.observeOn(scheduler.main());
    }

}
