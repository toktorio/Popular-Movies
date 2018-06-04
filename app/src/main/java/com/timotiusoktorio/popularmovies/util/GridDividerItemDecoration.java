package com.timotiusoktorio.popularmovies.util;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridDividerItemDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public GridDividerItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    private GridDividerItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}