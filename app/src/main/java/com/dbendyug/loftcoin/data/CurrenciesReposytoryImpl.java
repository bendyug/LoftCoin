package com.dbendyug.loftcoin.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

@Singleton
public class CurrenciesReposytoryImpl implements CurrenciesReposytory {

    private static final String CURRENCY_KEY = "currency_key";
    private Context context;


    @Inject
    CurrenciesReposytoryImpl(Context context) {
        this.context = context;
    }

    @Override
    public List<Currency> getAvailableCurrencies() {
        return Arrays.asList(Currency.values());
    }

    @Override
    public Currency getCurrentCurrency() {
        return Currency.valueOf(getSharedPreferences().getString(CURRENCY_KEY, Currency.RUB.currencyName()));
    }

    @Override
    public void setCurrentCurrency(Currency currency) {
        getSharedPreferences().edit().putString(CURRENCY_KEY, currency.currencyName()).apply();
    }

    private SharedPreferences getSharedPreferences(){
        return context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
    }
}
