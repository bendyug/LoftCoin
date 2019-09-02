package com.dbendyug.loftcoin.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {CoinEntity.class},
        version = 3
        )
public abstract class LoftDb extends RoomDatabase {

    public abstract CoinDao coins();
}
