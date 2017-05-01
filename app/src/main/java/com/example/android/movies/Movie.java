package com.example.android.movies;

/**
 * Created by Joseph Costlow on 18-Feb-17.
 */

public class Movie {

    private static final String TAG = Movie.class.getSimpleName();

    public String moviePosterPath;
    public String movieTitle;
    public String movieReleaseDate;
    public String movieOverview;
    public int movieId;
    public String movieRating;

    public Movie(String path, String title, String releaseDate, String overview, int id, String rating) {
        this.moviePosterPath = path;
        this.movieTitle = title;
        this.movieReleaseDate = releaseDate;
        this.movieOverview = overview;
        this.movieId = id;
        this.movieRating = rating;

    }
}
