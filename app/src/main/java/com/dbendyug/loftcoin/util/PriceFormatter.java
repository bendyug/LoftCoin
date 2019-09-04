package com.dbendyug.loftcoin.util;

public interface PriceFormatter extends DoubleFormatter {
    String format(double value, String symbol);
}
