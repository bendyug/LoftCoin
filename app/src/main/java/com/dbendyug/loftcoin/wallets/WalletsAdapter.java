package com.dbendyug.loftcoin.wallets;

import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.db.WalletEntity;
import com.dbendyug.loftcoin.util.ImageURLFormatter;
import com.dbendyug.loftcoin.util.PriceFormatter;
import com.dbendyug.loftcoin.util.StableIdDiff;
import com.squareup.picasso.Picasso;


import javax.inject.Inject;

public class WalletsAdapter extends ListAdapter<WalletEntity.View, WalletsAdapter.ViewHolder> {

    private PriceFormatter priceFormatter;
    private ImageURLFormatter imageURLFormatter;
    private LayoutInflater layoutInflater;

    @Inject
    WalletsAdapter(PriceFormatter priceFormatter, ImageURLFormatter imageURLFormatter) {
        super(new StableIdDiff<>());
        this.priceFormatter = priceFormatter;
        this.imageURLFormatter = imageURLFormatter;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.item_list_wallets, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WalletEntity.View wallet = getItem(position);
        Picasso.get().load(imageURLFormatter.format(wallet.coinId())).into(holder.logo);
        holder.symbol.setText(wallet.symbol());
        holder.coinBalance.setText(priceFormatter.format(wallet.coinBalance(), wallet.symbol()));
        holder.currencyBalance.setText(priceFormatter.format(wallet.currencyBalance()));
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutInflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView logo;
        TextView symbol;
        TextView coinBalance;
        TextView currencyBalance;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            logo = itemView.findViewById(R.id.logo);
            symbol = itemView.findViewById(R.id.symbol);
            coinBalance = itemView.findViewById(R.id.coin_balance);
            currencyBalance = itemView.findViewById(R.id.currency_balance);

            logo.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2);
                }
            });
            logo.setClipToOutline(true);
        }
    }
}
