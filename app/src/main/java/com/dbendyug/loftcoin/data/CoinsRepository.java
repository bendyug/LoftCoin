package com.dbendyug.loftcoin.data;

import com.dbendyug.loftcoin.util.Consumer;

import java.util.List;

public interface CoinsRepository {

    void listing(String convert, Consumer<List<Coin>> onSuccess, Consumer<Throwable> onError);
}
