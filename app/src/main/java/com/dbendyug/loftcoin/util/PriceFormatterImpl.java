package com.dbendyug.loftcoin.util;

import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class PriceFormatterImpl implements PriceFormatter {

    private Locale locale;

    public PriceFormatterImpl(Context context){

        LocaleListCompat localeList = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
        locale = localeList.get(0);
    }

    @Override
    public String format(double value) {
        return NumberFormat.getCurrencyInstance(locale).format(value);
    }
}
