package com.dbendyug.loftcoin.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@AutoValue
@Entity (tableName = "coins")
public abstract class CoinEntity {

    @PrimaryKey
    @AutoValue.CopyAnnotations
    public abstract long id();

    public abstract String symbol();
    public abstract double price();
    public abstract double change24h();

    public static CoinEntity create(long id, String symbol, double price, double change24h){
        return new AutoValue_CoinEntity(id, symbol, price, change24h);
    }
}
