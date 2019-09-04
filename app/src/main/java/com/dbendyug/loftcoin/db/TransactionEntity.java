package com.dbendyug.loftcoin.db;

import androidx.room.ColumnInfo;
import androidx.room.DatabaseView;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.auto.value.AutoValue;

@AutoValue
@Entity(tableName = "transactions")
public abstract class TransactionEntity implements StableId {

    @PrimaryKey(autoGenerate = true)
    @AutoValue.CopyAnnotations
    @Override
    public abstract long id();

    public abstract long timestamp();

    public abstract double amount();

    @ColumnInfo(name = "wallet_id")
    @AutoValue.CopyAnnotations
    public abstract long walletId();

    public static TransactionEntity create(long id, long timestamp, double amount, long walletId) {
        return new AutoValue_TransactionEntity(id, timestamp, amount, walletId);
    }


    @AutoValue
    @DatabaseView(
            viewName = "transactions_view",
            value = "SELECT t.id, t.timestamp, t.wallet_id, c.symbol, " +
                    "t.amount AS coinAmount, " +
                    "(c.price * t.amount) AS currencyAmount " +
                    "FROM transactions AS t " +
                    "INNER JOIN wallets AS w ON t.wallet_id=w.id " +
                    "INNER JOIN coins AS c ON w.coin_id=c.id"
    )
    public static abstract class View implements StableId {
        @Override
        public abstract long id();

        public abstract String symbol();

        public abstract long timestamp();

        public abstract double coinAmount();

        public abstract double currencyAmount();

        public static TransactionEntity.View create(long id, String symbol, long timestamp, double coinAmount, double currencyAmount) {
            return new AutoValue_TransactionEntity_View(id, symbol, timestamp, coinAmount, currencyAmount);
        }

    }

}
