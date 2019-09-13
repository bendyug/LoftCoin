package com.dbendyug.loftcoin.db;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Wallet implements StableId<String> {

    public abstract double coinBalance();

    public abstract CoinEntity coinEntity();

    public double currencyBalance() {
        return coinBalance() * coinEntity().price();
    }

    public static Wallet create(String id, Double coinBalance, CoinEntity coinEntity) {
        return new AutoValue_Wallet(id, coinBalance, coinEntity);
    }
}
