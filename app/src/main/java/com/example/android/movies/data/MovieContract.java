package com.example.android.movies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Joseph Costlow on 12-Mar-17.
 */

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoritesEntry implements BaseColumns {

        public static final Uri CONTENT_URI_FAVORITES = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME_FAVORITES = "favorites";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_VOTE_AVG = "voteAvg";
    }
}
