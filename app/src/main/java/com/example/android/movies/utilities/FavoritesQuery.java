package com.example.android.movies.utilities;

import android.content.Context;
import android.database.Cursor;

import com.example.android.movies.Movie;
import com.example.android.movies.data.MovieContract;

import java.util.ArrayList;

/**
 * Created by Joseph Costlow on 17-Mar-17.
 */

public class FavoritesQuery {

    FavoritesQuery() {
    }

    /**
     * Iterates through each row of the local database and appends to ArrayList<Movie> to be
     * displayed in MainActivity RecyclerView.
     *
     * @param context
     * @movieList list of favorite movies to be returned
     * @return
     */

    public static ArrayList<Movie> fetchFavorites(Context context) {

        ArrayList<Movie> movieList = new ArrayList<>();
        Cursor cursor = null;

        cursor = context.getContentResolver().query(MovieContract.FavoritesEntry.CONTENT_URI_FAVORITES,
                null,
                null,
                null,
                null);

        int i = 0;

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            cursor.moveToPosition(i);
            String posterPath = cursor.getString(1);
            String overview = cursor.getString(2);
            String releaseDate = cursor.getString(3);
            int movieId = cursor.getInt(4);
            String title = cursor.getString(5);
            String voteAvg = cursor.getString(6);

            Movie movie = new Movie(posterPath, title, releaseDate, overview, movieId, voteAvg);
            movieList.add(movie);
            i++;
        }

        cursor.close();

        return movieList;
    }

    /**
     * Compares the movie displayed in DetailActivity, by unique movie ID from online database, to
     * unique movie IDs of favorite movies, to eliminate duplication before writing to local
     * database.
     *
     * @param context
     * @param currentMovieId unique movie ID from local database (if already a favorite) or from
     *                       online database that has been displayed in Detail Activity
     * @return
     */

    public static boolean compareFavoritesMovieId(Context context, int currentMovieId) {

        Cursor cursor;
        boolean isFavorite = false;

        String[] projection = {MovieContract.FavoritesEntry.COLUMN_MOVIE_ID};

        cursor = context.getContentResolver().query(
                MovieContract.FavoritesEntry.CONTENT_URI_FAVORITES,
                projection,
                null,
                null,
                null);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int favoriteMovieId = cursor.getInt(0);

            if (favoriteMovieId == currentMovieId) {
                isFavorite = true;
            }
        }

        cursor.close();

        return isFavorite;
    }

    /**
     * Selects the individual movie to be deleted from local database. Query by unique movie ID
     * from online database and returns _ID column contents of individual movie.
     *
     * @param context
     * @param currentMovieId unique movie ID from local database that has been displayed in Detail
     *                       Activity
     * @return _ID column of individual movie
     */

    public static long favoriteToBeDeleted(Context context, int currentMovieId) {

        Cursor cursor;

        String[] projection = {
                MovieContract.FavoritesEntry._ID,
                MovieContract.FavoritesEntry.COLUMN_MOVIE_ID};

        cursor = context.getContentResolver().query(
                MovieContract.FavoritesEntry.CONTENT_URI_FAVORITES,
                projection,
                null,
                null,
                null);

        long uniqueIdIndex = 0;
        int i = 0;

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            cursor.moveToPosition(i);
            int favoriteId = cursor.getColumnIndex(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID);
            int favoriteMovieId = cursor.getInt(favoriteId);

            if (favoriteMovieId == currentMovieId) {
                int favoriteUniqueId = cursor.getColumnIndex(MovieContract.FavoritesEntry._ID);
                uniqueIdIndex = (long) cursor.getInt(favoriteUniqueId);
                return uniqueIdIndex;
            }

            i++;
        }

        cursor.close();

        return uniqueIdIndex;
    }
}
