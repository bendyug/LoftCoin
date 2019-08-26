package com.dbendyug.loftcoin.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.dbendyug.loftcoin.R;

import java.util.Objects;

public class WelcomePagerAdapter extends RecyclerView.Adapter<WelcomePagerAdapter.ViewHolder> {

    private LayoutInflater inflater;

    private static final int[] IMAGES = {
            R.drawable.welcome_1,
            R.drawable.welcome_2,
            R.drawable.welcome_3
    };

    private static final int[] TITLES = {
            R.string.title_1,
            R.string.title_2,
            R.string.title_3
    };

    private static final int[] SUBTITLES = {
            R.string.subtitle_1,
            R.string.subtitle_2,
            R.string.subtitle_3
    };

    public WelcomePagerAdapter(@NonNull LayoutInflater inflater) {
        this.inflater = Objects.requireNonNull(inflater);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.welcome_page, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.welcomeImage.setImageResource(IMAGES[position]);
        holder.welcomeTitle.setText(TITLES[position]);
        holder.welcomeSubtitle.setText(SUBTITLES[position]);
    }

    @Override
    public int getItemCount() {
        return IMAGES.length;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView welcomeImage;
        private TextView welcomeTitle;
        private TextView welcomeSubtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            welcomeImage = itemView.findViewById(R.id.welcome_image);
            welcomeTitle = itemView.findViewById(R.id.title);
            welcomeSubtitle = itemView.findViewById(R.id.subtitle);
        }
    }
}
