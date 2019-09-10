package com.dbendyug.loftcoin.db;

import com.google.auto.value.AutoValue;

import java.util.Date;

@AutoValue
public abstract class Transaction implements StableId<String> {

    public abstract Double coinAmount();

    public abstract Date timestamp();


    public abstract Wallet wallet();

    public double currencyAmount() {
        return coinAmount() * wallet().coinEntity().price();
    }

    public static Transaction create(String id, Double coinAmount, Date timestamp, Wallet wallet) {
        return new AutoValue_Transaction(id, coinAmount, timestamp, wallet);
    }
}
