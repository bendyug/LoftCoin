package com.dbendyug.loftcoin.util;

import android.content.Context;
import android.util.Pair;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.dbendyug.loftcoin.data.CurrenciesReposytory;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

public class PriceFormatterImpl implements PriceFormatter {


    private CurrenciesReposytory currenciesReposytory;

    @Inject
    PriceFormatterImpl(CurrenciesReposytory currenciesReposytory){

        this.currenciesReposytory = currenciesReposytory;
    }

    @Override
    public String format(double value) {
        Pair<Currency, Locale> pair = currenciesReposytory.getPair();
        Locale locale = Objects.requireNonNull(pair.second);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        if (value > 1d){
            return numberFormat.format(value);
        } else{
            numberFormat.setMaximumFractionDigits(6);
            return numberFormat.format(value);
        }
    }
}
