package com.dbendyug.loftcoin.exchangerates;

import androidx.annotation.Nullable;

import com.dbendyug.loftcoin.db.CoinEntity;
import com.google.auto.value.AutoValue;

import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class ExchangeRatesUiState {

    abstract List<CoinEntity> exchangeRates();

    @Nullable
    abstract String error();

    abstract boolean isRefreshing();

    static ExchangeRatesUiState loading() {
        return new AutoValue_ExchangeRatesUiState(Collections.emptyList(), null, true);
    }

    static ExchangeRatesUiState error(Throwable error) {
        return new AutoValue_ExchangeRatesUiState(Collections.emptyList(), error.getMessage(), false);
    }

    static ExchangeRatesUiState success(List<CoinEntity> exchangeRates) {
        return new AutoValue_ExchangeRatesUiState(exchangeRates, null, false);
    }
}
