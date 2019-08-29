package com.dbendyug.loftcoin.data;

import android.util.Pair;

import java.util.Currency;
import java.util.Locale;

public interface CurrenciesReposytory {

    Pair<Currency, Locale> getPair();
}
