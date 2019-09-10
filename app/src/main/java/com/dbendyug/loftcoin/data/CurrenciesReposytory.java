package com.dbendyug.loftcoin.data;

import java.util.List;

import io.reactivex.Observable;

public interface CurrenciesReposytory {

    List<Currency> getAvailableCurrencies();

    Currency getCurrentCurrency();

    void setCurrentCurrency(Currency currency);

    Observable<Currency> currentCurrency();
}
