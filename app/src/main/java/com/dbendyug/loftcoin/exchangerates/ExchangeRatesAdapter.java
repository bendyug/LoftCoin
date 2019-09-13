package com.dbendyug.loftcoin.exchangerates;

import android.content.res.Resources;
import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.dbendyug.loftcoin.R;
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.util.Change24hFormatter;
import com.dbendyug.loftcoin.util.ImageURLFormatter;
import com.dbendyug.loftcoin.util.PriceFormatter;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

public class ExchangeRatesAdapter extends ListAdapter<CoinEntity, ExchangeRatesAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private ImageURLFormatter imageURLFormatter;
    private PriceFormatter priceFormatter;
    private Change24hFormatter change24hFormatter;

    @Inject
    ExchangeRatesAdapter(ImageURLFormatter imageURLFormatter, PriceFormatter priceFormatter, Change24hFormatter change24hFormatter) {
        super(new DiffUtil.ItemCallback<CoinEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull CoinEntity oldItem, @NonNull CoinEntity newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CoinEntity oldItem, @NonNull CoinEntity newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });

        this.imageURLFormatter = imageURLFormatter;
        this.priceFormatter = priceFormatter;
        this.change24hFormatter = change24hFormatter;
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(inflater.inflate(R.layout.items_list_coin_exchange_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CoinEntity coinEntity = getItem(position);

        Picasso.get().load(imageURLFormatter.format(coinEntity.id())).into(holder.coinImage);

        holder.coinSymbol.setText(coinEntity.symbol());
        holder.coinPrice.setText(priceFormatter.format(coinEntity.price()));
        holder.coinChange24h.setText(change24hFormatter.format(coinEntity.change24h()));

        Resources resources = holder.itemView.getResources();
        Resources.Theme theme = holder.itemView.getContext().getTheme();
        if (coinEntity.change24h() > 0) {
            holder.coinChange24h.setTextColor(ResourcesCompat.getColor(resources, R.color.price_change_positive, theme));
        } else {
            holder.coinChange24h.setTextColor(ResourcesCompat.getColor(resources, R.color.price_change_negative, theme));
        }
        if (position % 2 == 0) {
            holder.itemView.setBackgroundResource(R.color.dark_two);
        } else {
            holder.itemView.setBackgroundResource(R.color.dark_three);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).id();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        inflater = LayoutInflater.from(recyclerView.getContext());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView coinImage;
        private TextView coinSymbol;
        private TextView coinPrice;
        private TextView coinChange24h;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            coinImage = itemView.findViewById(R.id.coin_image);
            coinSymbol = itemView.findViewById(R.id.coin_symbol);
            coinPrice = itemView.findViewById(R.id.coin_price);
            coinChange24h = itemView.findViewById(R.id.coin_change_24h);
            coinImage.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2);
                }
            });
            coinImage.setClipToOutline(true);
        }
    }
}
