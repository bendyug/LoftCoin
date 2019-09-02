package com.dbendyug.loftcoin.data;

import android.util.Pair;

import java.util.List;
import java.util.Locale;

public interface CurrenciesReposytory {

    List<Currency> getAvailableCurrencies();

    Currency getCurrentCurrency();

    void setCurrentCurrency(Currency currency);
}
