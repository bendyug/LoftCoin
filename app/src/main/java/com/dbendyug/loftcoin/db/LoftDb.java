package com.dbendyug.loftcoin.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {
        CoinEntity.class,
        WalletEntity.class,
        TransactionEntity.class},
        views = {
                WalletEntity.View.class,
                TransactionEntity.View.class
        },
        version = 6
)
public abstract class LoftDb extends RoomDatabase {

    public abstract CoinDao coins();

    public abstract WalletDao wallets();
}
