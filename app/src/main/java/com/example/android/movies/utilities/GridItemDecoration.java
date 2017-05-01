package com.example.android.movies.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Joseph Costlow on 20-Feb-17.
 */

public class GridItemDecoration extends RecyclerView.ItemDecoration {

    private int spacing;

    public GridItemDecoration(Context context, int space) {
        this.spacing = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(spacing, spacing, spacing, spacing);
    }
}
