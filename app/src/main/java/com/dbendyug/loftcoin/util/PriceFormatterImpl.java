package com.dbendyug.loftcoin.util;

import com.dbendyug.loftcoin.data.CurrenciesReposytory;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

public class PriceFormatterImpl implements PriceFormatter {


    private CurrenciesReposytory currenciesReposytory;
    private Provider<Locale> locale;

    @Inject
    PriceFormatterImpl(CurrenciesReposytory currenciesReposytory,
                       Provider<Locale> locale) {

        this.currenciesReposytory = currenciesReposytory;
        this.locale = locale;
    }

    @Override
    public String format(double value) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale.get());
        if (value < 1d && value > -1d) {
            numberFormat.setMaximumFractionDigits(6);
            setCurrencySymbol(numberFormat);
            return numberFormat.format(value);
        } else {
            setCurrencySymbol(numberFormat);
            return numberFormat.format(value);
        }
    }

    DecimalFormat setCurrencySymbol(NumberFormat numberFormat) {
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(currenciesReposytory.getCurrentCurrency().getCurrencySymbol());
        decimalFormat.setDecimalFormatSymbols(symbols);
        return decimalFormat;
    }

    @Override
    public String format(double value, String sign) {
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale.get());
        numberFormat.setMaximumFractionDigits(6);
        DecimalFormat decimalFormat = (DecimalFormat) numberFormat;
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol(sign);
        decimalFormat.setDecimalFormatSymbols(symbols);
        return numberFormat.format(value).replace('\u00A0', ' ').trim();
    }

}
