package com.dbendyug.loftcoin.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Observable;

@Dao
public interface CoinDao {

//    @Query("SELECT * FROM coins")
//    LiveData<List<CoinEntity>> fetchAll();

    @Query("SELECT * FROM coins")
    Observable<List<CoinEntity>> fetchAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CoinEntity> coins);
}
