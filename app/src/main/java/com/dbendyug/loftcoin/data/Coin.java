package com.dbendyug.loftcoin.data;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

class Coin {

    @SerializedName("id")
    int id;

    @SerializedName("symbol")
    String symbol;

    @SerializedName("name")
    String name;

    @SerializedName("quote")
    Map<String, Quote> quote;
}
