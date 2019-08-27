package com.dbendyug.loftcoin.exchangerates;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.data.Coin;
import com.dbendyug.loftcoin.data.CoinsRepository;
import com.dbendyug.loftcoin.data.Quote;
import com.dbendyug.loftcoin.util.Change24hFormatter;
import com.dbendyug.loftcoin.util.Change24hFormatterImpl;
import com.dbendyug.loftcoin.util.ImageURLFormatter;
import com.dbendyug.loftcoin.util.ImageURLFormatterImpl;
import com.dbendyug.loftcoin.util.PriceFormatter;
import com.dbendyug.loftcoin.util.PriceFormatterImpl;

import java.util.ArrayList;
import java.util.List;


public class ExchangeRatesViewModel extends ViewModel {

    private CoinsRepository coinsRepository;
    private final PriceFormatter priceFormatter;
    private final Change24hFormatter change24hFormatter;
    private final ImageURLFormatter imageURLFormatter;

    private MutableLiveData<Throwable> error = new MutableLiveData<>();
    private MutableLiveData<List<CoinExchangeRate>> coinData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isRefreshing = new MutableLiveData<>();
    private String currency;
//    private String currencySymbol;

    private ExchangeRatesViewModel(CoinsRepository coinsRepository,
                                   PriceFormatter priceFormatter,
                                   Change24hFormatter change24hFormatter,
                                   ImageURLFormatter imageURLFormatter){
        this.coinsRepository = coinsRepository;
        this.priceFormatter = priceFormatter;
        this.change24hFormatter = change24hFormatter;
        this.imageURLFormatter = imageURLFormatter;
        setCurrency("RUB");
    }

    public void setCurrency(String currency){
        this.currency = currency;
        refresh();
    }

//    public void setCurrencySymbol(String currency, Context context){
//
//    }

    void refresh() {
        isRefreshing.postValue(true);
        coinsRepository.listing(currency, coins -> {
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
            coinData.postValue(exchangeRates);
            isRefreshing.postValue(false);
        }, value -> {
            error.postValue(value);
            isRefreshing.postValue(false);
        });
    }

    LiveData<Boolean> isRefreshing(){
        return isRefreshing;
    }

    LiveData<Throwable> error(){
        return error;
    }

    LiveData<List<CoinExchangeRate>> coinData() {
        return coinData;
    }

    static class Factory implements ViewModelProvider.Factory{

        private Context context;

        Factory(Context context){
            this.context = context;
        }

        @SuppressWarnings("unchecked")
        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return (T) new ExchangeRatesViewModel(CoinsRepository.get(),
                    new PriceFormatterImpl(context),
                    new Change24hFormatterImpl(context),
                    new ImageURLFormatterImpl());
        }
    }


}
