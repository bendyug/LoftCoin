package com.dbendyug.loftcoin.data;

import androidx.annotation.NonNull;

import com.dbendyug.loftcoin.util.Consumer;

import java.util.List;

public interface CoinsRepository {

    @NonNull
    static CoinsRepository get(){
        return CoinsRepositoryImplementation.get();
    }

    void listing(String convert, Consumer<List<Coin>> onSuccess, Consumer<Throwable> onError);
}
