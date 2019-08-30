package com.dbendyug.loftcoin.exchangerates;

import android.util.Pair;

import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.dbendyug.loftcoin.data.Coin;
import com.dbendyug.loftcoin.data.CurrenciesReposytory;
import com.dbendyug.loftcoin.data.Quote;
import com.dbendyug.loftcoin.util.Change24hFormatter;
import com.dbendyug.loftcoin.util.Function;
import com.dbendyug.loftcoin.util.ImageURLFormatter;
import com.dbendyug.loftcoin.util.PriceFormatter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

import dagger.Reusable;

@Reusable
class ExchangeRatesMapper implements Function<List<Coin>, List<CoinExchangeRate>> {

    private Change24hFormatter change24hFormatter;
    private ImageURLFormatter imageURLFormatter;
    private PriceFormatter priceFormatter;
    private CurrenciesReposytory currenciesReposytory;


    @Inject ViewModelProvider.Factory viewModelProviderFactory;

    @Inject
    ExchangeRatesMapper (Change24hFormatter change24hFormatter,
                         ImageURLFormatter imageURLFormatter,
                         PriceFormatter priceFormatter,
                         CurrenciesReposytory currenciesReposytory)
                        {

        this.change24hFormatter = change24hFormatter;
        this.imageURLFormatter = imageURLFormatter;
        this.priceFormatter = priceFormatter;
        this.currenciesReposytory = currenciesReposytory;
    }





    @Override
    public List<CoinExchangeRate> apply(List<Coin> coins, String currency) {


        List<CoinExchangeRate> exchangeRates = new ArrayList<>(coins.size());
            for (Coin coin : coins){
                CoinExchangeRate.Builder builder = CoinExchangeRate.build()
                        .id(coin.getId())
                        .imageUrl(imageURLFormatter.format(coin.getId()))
                        .symbol(coin.getSymbol());
                Quote quote = coin.getQuote().get(currency);
                if (quote != null) {
                    builder.price(priceFormatter.format(quote.getPrice()))
                            .change24h(change24hFormatter.format(quote.getChange24h()))
                            .isChange24hPositive(quote.getChange24h() > 0d);
                }
                exchangeRates.add(builder.build());
            }
            return Collections.unmodifiableList(exchangeRates);
    }
}
