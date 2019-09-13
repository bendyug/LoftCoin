package com.dbendyug.loftcoin.exchangerates;

import com.google.auto.value.AutoValue;

@AutoValue
abstract class CoinExchangeRate {

    static CoinExchangeRate.Builder build() {
        return new AutoValue_CoinExchangeRate.Builder();
    }

    abstract int id();

    abstract String symbol();

    abstract String price();

    abstract String change24h();

    abstract String imageUrl();

    abstract boolean isChange24hPositive();

    @AutoValue.Builder
    abstract static class Builder {

        abstract Builder id(int id);

        abstract Builder symbol(String symbol);

        abstract Builder price(String price);

        abstract Builder change24h(String change24h);

        abstract Builder imageUrl(String imageUrl);

        abstract Builder isChange24hPositive(boolean isChange24hPositive);

        abstract CoinExchangeRate build();
    }

}
