package com.dbendyug.loftcoin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.dbendyug.loftcoin.R;

import java.util.Objects;

public class WelcomePagerAdapter extends PagerAdapter {

    private final LayoutInflater inflater;
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

    @Override
    public int getCount() {
        return IMAGES.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.welcome_page, container, false);
        container.addView(view, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));
        view.<ImageView>findViewById(R.id.welcome_image).setImageResource(IMAGES[position]);
        view.<TextView>findViewById(R.id.title).setText(TITLES[position]);
        view.<TextView>findViewById(R.id.subtitle).setText(SUBTITLES[position]);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return Objects.equals(view, object);
    }
}
