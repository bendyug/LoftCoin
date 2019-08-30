package com.dbendyug.loftcoin.data;

import android.util.Pair;

import java.util.Currency;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
class CurrenciesReposytoryImpl implements CurrenciesReposytory {

    private Provider<Locale> locale;


    @Inject
    CurrenciesReposytoryImpl(Provider<Locale> locale) {
        this.locale = locale;
    }

    @Override
    public Pair<Currency, Locale> getPair() {
     return Pair.create(Currency.getInstance(locale.get()), locale.get());
    }
}
