package com.dbendyug.loftcoin.data;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.Map;

public class Coin {

    @SerializedName("id")
    int id;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("name")
    String name;

    @SerializedName("quote")
    Map<String, Quote> quote;

    public int getId() {
        return id;
    }

    public String getSymbol() {
        if (symbol == null) {
            return null;
        } else {
            return symbol;
        }
    }

    public String getName() {
        if (name == null) {
            return null;
        } else {
            return name;
        }
    }

    public Map<String, Quote> getQuote() {
        if (quote == null) {
            return Collections.emptyMap();
        } else {
            return Collections.unmodifiableMap(quote);
        }
    }
}
