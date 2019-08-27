package com.dbendyug.loftcoin.util;

import android.content.Context;

import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import java.text.NumberFormat;
import java.util.Locale;

public class Change24hFormatterImpl implements Change24hFormatter {

    private Locale locale;

    public Change24hFormatterImpl(Context context){

        LocaleListCompat localeList = ConfigurationCompat.getLocales(context.getResources().getConfiguration());
        locale = localeList.get(0);
    }

    @Override
    public String format(double value) {
        return String.format(locale, "%.4f%%", value);
    }
}
