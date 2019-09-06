package com.dbendyug.loftcoin.wallets;

import android.content.res.Resources;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.db.TransactionEntity;
import com.dbendyug.loftcoin.util.PriceFormatter;
import com.dbendyug.loftcoin.util.StableIdDiff;

import javax.inject.Inject;

public class TransactionAdapter extends ListAdapter<TransactionEntity.View, TransactionAdapter.ViewHolder> {

    private PriceFormatter priceFormatter;
    private LayoutInflater layoutInflater;

    @Inject
    TransactionAdapter(PriceFormatter priceFormatter) {
        super(new StableIdDiff<>());
        this.priceFormatter = priceFormatter;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_list_transactions, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TransactionEntity.View transaction = getItem(position);

        holder.coinAmount.setText(priceFormatter.format(transaction.coinAmount(), transaction.symbol()));
        holder.currencyAmount.setText(priceFormatter.format(transaction.currencyAmount()));

        Resources resources = holder.itemView.getResources();
        Resources.Theme theme = holder.itemView.getContext().getTheme();

        if (transaction.coinAmount() > 0d) {
            holder.currencyAmount.setTextColor(ResourcesCompat.getColor(resources, R.color.price_change_positive, theme));
            holder.arrow.setImageResource(R.drawable.ic_positive_transaction);
        } else {
            holder.currencyAmount.setTextColor(ResourcesCompat.getColor(resources, R.color.price_change_negative, theme));
            holder.arrow.setImageResource(R.drawable.ic_negative_transaction);
        }

        holder.timestamp.setText(DateUtils.formatDateTime(holder.itemView.getContext(), transaction.timestamp(), DateUtils.FORMAT_SHOW_YEAR));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        layoutInflater = LayoutInflater.from(recyclerView.getContext());
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView arrow;
        TextView coinAmount;
        TextView currencyAmount;
        TextView timestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            arrow = itemView.findViewById(R.id.transaction_arrow);
            coinAmount = itemView.findViewById(R.id.coin_amount);
            currencyAmount = itemView.findViewById(R.id.currency_amount);
            timestamp = itemView.findViewById(R.id.timestamp);
        }
    }
}
