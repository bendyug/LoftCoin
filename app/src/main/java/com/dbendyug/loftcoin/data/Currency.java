package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.R;

public enum Currency {
    USD(R.string.USD, "$"),
    EUR(R.string.EUR, "€"),
    RUB(R.string.RUB, "\u20BD");

    private int nmeResId;
    private String currencySymbol;

    Currency(int nmeResId, String currencySymbol) {
        this.nmeResId = nmeResId;
        this.currencySymbol = currencySymbol;
    }

    public int getCurrencyId() {
        return nmeResId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public String currencyName(){
        return name();
    }
}
