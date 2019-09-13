package com.dbendyug.loftcoin.converter;

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
import com.dbendyug.loftcoin.db.CoinEntity;
import com.dbendyug.loftcoin.util.ImageURLFormatter;
import com.dbendyug.loftcoin.util.StableIdDiff;
import com.squareup.picasso.Picasso;

import java.util.Locale;
import java.util.Objects;

import javax.inject.Inject;

public class ConverterCoinsAdapter extends ListAdapter<CoinEntity, ConverterCoinsAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    @Inject
    ImageURLFormatter imageURLFormatter;

    @Inject
    ConverterCoinsAdapter() {
        super(new StableIdDiff<>());
        setHasStableIds(true);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(layoutInflater.inflate(R.layout.item_list_coins_bottom_dialog, parent, false));
        holder.symbol.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2f);
            }
        });
        holder.symbol.setClipToOutline(true);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CoinEntity coin = Objects.requireNonNull(getItem(position));
        Picasso.get().load(imageURLFormatter.format(coin.id())).into(holder.symbol);
        holder.name.setText(String.format(Locale.US, "%s | %s",
                coin.name(), coin.symbol()));

    }

    @Override
    public long getItemId(int position) {
        return Objects.requireNonNull(getItem(position)).id();
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        layoutInflater = LayoutInflater.from(recyclerView.getContext());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView symbol;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            symbol = itemView.findViewById(R.id.symbol);
            name = itemView.findViewById(R.id.name);

            symbol.setOutlineProvider(new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), view.getWidth() / 2);
                }
            });
            symbol.setClipToOutline(true);
        }
    }
}
