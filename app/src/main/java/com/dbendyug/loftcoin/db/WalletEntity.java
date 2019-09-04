package com.dbendyug.loftcoin.db;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@AutoValue
@Entity(tableName = "wallets")
public abstract class WalletEntity implements StableId {

    @PrimaryKey(autoGenerate = true)
    @AutoValue.CopyAnnotations
    @Override
    public abstract long id();

    public abstract double balance();

    @ColumnInfo(name = "coin_id")
    @AutoValue.CopyAnnotations
    public abstract long coinId();

    public static WalletEntity create(long id, double balance, long coinId) {
        return new AutoValue_WalletEntity(id, balance, coinId);
    }

    @AutoValue
    @DatabaseView(
            viewName = "wallets_view",
            value = "SELECT wallets.id, c.symbol, " +
                    "wallets.balance AS coinBalance, " +
                    "wallets.balance * c.price AS currencyBalance, " +
                    "wallets.coin_id as coinId " +
                    "FROM wallets AS wallets INNER JOIN coins AS c ON wallets.coin_id=c.id")
    public static abstract class View implements StableId {
        @Override
        public abstract long id();

        public abstract String symbol();

        public abstract double coinBalance();

        public abstract double currencyBalance();

        public abstract long coinId();

        public static WalletEntity.View create(long id, String symbol, double coinBalance, double currencyBalance, long coinId) {
            return new AutoValue_WalletEntity_View(id, symbol, coinBalance, currencyBalance, coinId);
        }

    }
}
