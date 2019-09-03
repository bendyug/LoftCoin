package com.dbendyug.loftcoin.util;

import android.content.Context;
import android.util.Pair;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.dbendyug.loftcoin.data.CurrenciesReposytory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Provider;

public class PriceFormatterImpl implements PriceFormatter {


    private CurrenciesReposytory currenciesReposytory;
    private Provider<Locale> locale;

    @Inject
    PriceFormatterImpl(CurrenciesReposytory currenciesReposytory,
                       Provider<Locale> locale){

        this.currenciesReposytory = currenciesReposytory;
        this.locale = locale;
    }

    @Override
    public String format(double value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale.get());
        if (value > 1d){
            setCurrencySymbol(numberFormat);
            return numberFormat.format(value);
        } else{
            numberFormat.setMaximumFractionDigits(6);
            setCurrencySymbol(numberFormat);
            return numberFormat.format(value);
        }

    }

    public DecimalFormat setCurrencySymbol(NumberFormat numberFormat) {
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(currenciesReposytory.getCurrentCurrency().getCurrencySymbol());
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat;
    }
}
