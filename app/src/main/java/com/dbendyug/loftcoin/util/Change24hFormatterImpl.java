package com.dbendyug.loftcoin.util;

import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Provider;

import dagger.Reusable;

@Reusable
public class Change24hFormatterImpl implements Change24hFormatter {

    private Provider<Locale> locale;

    @Inject
    Change24hFormatterImpl(Provider<Locale> locale){

        this.locale = locale;
    }

    @Override
    public String format(double value) {
        return String.format(locale.get(), "%.4f%%", value);
    }
}
