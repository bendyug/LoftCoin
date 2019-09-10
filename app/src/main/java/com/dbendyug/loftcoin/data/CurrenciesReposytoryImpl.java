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

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

@Singleton
public class CurrenciesReposytoryImpl implements CurrenciesReposytory {

    private static final String CURRENCY_KEY = "currency_key";

    private SharedPreferences sharedPreferences;

    @Inject
    CurrenciesReposytoryImpl(Context context) {
        sharedPreferences = context.getSharedPreferences("currencies", Context.MODE_PRIVATE);
    }

    @Override
    public List<Currency> getAvailableCurrencies() {
        return Arrays.asList(Currency.values());
    }

    @Override
    public Currency getCurrentCurrency() {
        return Currency.valueOf(sharedPreferences.getString(CURRENCY_KEY, Currency.RUB.currencyName()));
    }

    @Override
    public void setCurrentCurrency(Currency currency) {
        sharedPreferences.edit().putString(CURRENCY_KEY, currency.currencyName()).apply();
    }

    @Override
    public Observable<Currency> currentCurrency() {
        return Observable.create(emitter -> {
            SharedPreferences.OnSharedPreferenceChangeListener listener = (prefs, key) -> {
                emitter.onNext(getCurrentCurrency());
            };
            emitter.setCancellable(() -> sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener));
            sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            emitter.onNext(getCurrentCurrency());
        });
    }
}
