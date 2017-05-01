package com.example.android.movies.utilities;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Joseph Costlow on 17-Feb-17.
 */

public class AutofitGrid {
    public static int calcNumberOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int numberOfColumns = (int) (dpWidth / 100);
        return numberOfColumns;
    }
}
