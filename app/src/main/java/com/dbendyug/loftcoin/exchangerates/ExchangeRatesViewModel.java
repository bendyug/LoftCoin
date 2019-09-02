package com.dbendyug.loftcoin.exchangerates;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.dbendyug.loftcoin.data.Coin;
import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.Currency;
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.util.Function;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;


public class ExchangeRatesViewModel extends ViewModel {

    private CoinsRepository coinsRepository;
//    private Function<List<Coin>, List<CoinExchangeRate>> exchangeRatesMapper;
//    private CurrenciesReposytory currenciesReposytory;

    private MutableLiveData<Throwable> error = new MutableLiveData<>();

//    private MutableLiveData<List<CoinExchangeRate>> coinData = new MutableLiveData<>();

    private LiveData<List<CoinEntity>> coinData;

    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();

    private CurrenciesReposytory currenciesReposytory;

    private Currency currency;
//    private String currency;


    @Inject
    ExchangeRatesViewModel(CoinsRepository coinsRepository,
                           CurrenciesReposytory currenciesReposytory
//                           Function<List<Coin>, List<CoinExchangeRate>> exchangeRatesMapper,
//                           CurrenciesReposytory currenciesReposytory
    ) {
        this.coinsRepository = coinsRepository;
//        this.exchangeRatesMapper = exchangeRatesMapper;
//        this.currenciesReposytory = currenciesReposytory;
        coinData = coinsRepository.listings();
        this.currenciesReposytory = currenciesReposytory;
    }

    public void setCurrency(Currency currency) {
        currenciesReposytory.setCurrentCurrency(currency);
        refresh();
    }

    void refresh() {
        isRefreshing.postValue(true);
        currency = currenciesReposytory.getCurrentCurrency();
        coinsRepository.refresh(currency.currencyName(), () -> isRefreshing.postValue(false),
                error -> {
                    isRefreshing.postValue(false);
                    this.error.postValue(error);
                });
    }

    LiveData<Boolean> isRefreshing() {
        return isRefreshing;
    }

    LiveData<Throwable> error() {
        return error;
    }

    LiveData<List<CoinEntity>> coinData() {
        return coinData;
    }

}
