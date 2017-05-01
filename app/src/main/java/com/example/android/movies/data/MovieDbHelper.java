package com.example.android.movies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.movies.data.MovieContract.FavoritesEntry;

/**
 * Created by Joseph Costlow on 12-Mar-17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_FAVORITES_TABLE =
                "CREATE TABLE " + FavoritesEntry.TABLE_NAME_FAVORITES + " (" +

                        FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoritesEntry.COLUMN_POSTER_PATH + " STRING NOT NULL, " +

                        FavoritesEntry.COLUMN_OVERVIEW + " STRING NOT NULL, " +

                        FavoritesEntry.COLUMN_RELEASE_DATE + " STRING NOT NULL, " +

                        FavoritesEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +

                        FavoritesEntry.COLUMN_MOVIE_TITLE + " STRING NOT NULL, " +

                        FavoritesEntry.COLUMN_VOTE_AVG + " STRING NOT NULL " + ");";

        db.execSQL(CREATE_FAVORITES_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavoritesEntry.TABLE_NAME_FAVORITES);
        onCreate(db);

    }
}
