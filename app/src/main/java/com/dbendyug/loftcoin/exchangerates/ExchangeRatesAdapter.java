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
import com.squareup.picasso.Picasso;

import java.util.Objects;

import javax.inject.Inject;

public class ExchangeRatesAdapter extends ListAdapter<CoinExchangeRate, ExchangeRatesAdapter.ViewHolder> {

    private LayoutInflater inflater;

    @Inject
    ExchangeRatesAdapter() {
        super(new DiffUtil.ItemCallback<CoinExchangeRate>() {
            @Override
            public boolean areItemsTheSame(@NonNull CoinExchangeRate oldItem, @NonNull CoinExchangeRate newItem) {
                return oldItem.id() == newItem.id();
            }

            @Override
            public boolean areContentsTheSame(@NonNull CoinExchangeRate oldItem, @NonNull CoinExchangeRate newItem) {
                return Objects.equals(oldItem, newItem);
            }
        });
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(inflater.inflate(R.layout.items_list_coin_exchange_rate, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        CoinExchangeRate rate= getItem(position);

        Picasso.get().load(rate.imageUrl()).into(holder.coinImage);
        holder.coinSymbol.setText(rate.symbol());
        holder.coinPrice.setText(rate.price());
        holder.coinChange24h.setText(rate.change24h());

        Resources resources = holder.itemView.getResources();
        Resources.Theme theme = holder.itemView.getContext().getTheme();
        if (rate.isChange24hPositive()){
            holder.coinChange24h.setTextColor(ResourcesCompat.getColor(resources, R.color.price_change_positive, theme));
        } else {
            holder.coinChange24h.setTextColor(ResourcesCompat.getColor(resources, R.color.price_change_negative, theme));
        }
        if (position % 2 == 0){
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
