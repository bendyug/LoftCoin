package com.dbendyug.loftcoin.util;

import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WalletsDecorator extends RecyclerView.ItemDecoration {
    private int dividerWidth;

    public WalletsDecorator(Context context, float dip) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        dividerWidth = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics));
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        final int position = parent.getChildAdapterPosition(view);
        if (position == 0) {
            outRect.set(dividerWidth, 0, dividerWidth, 0);
        } else {
            outRect.set(0, 0, dividerWidth, 0);
        }
    }
}