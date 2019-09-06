package com.dbendyug.loftcoin.data;

import android.util.Pair;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observable;

public interface CurrenciesReposytory {

    List<Currency> getAvailableCurrencies();

    Currency getCurrentCurrency();

    void setCurrentCurrency(Currency currency);

    Observable<Currency> currentCurrency();
}
