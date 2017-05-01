package com.example.android.movies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.movies.Movie;
import com.example.android.movies.R;
import com.example.android.movies.Review;
import com.example.android.movies.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by Joseph Costlow on 19-Mar-17.
 */

public class JsonUtils {

    JsonUtils() {
    }

    /**
     * Parses through JSON object to create individual movies to be displayed in MainActivity
     * RecyclerView.
     *
     * @param movieJson JSON object retrieved from online database query
     * @param context
     * @return list of movies to be displayed
     */

    public static ArrayList<Movie> extractMovieDataFromJson(String movieJson, Context context) {

//        JSON keys of key/value pairs in JSON object
        final String MDB_RESULTS = context.getString(R.string.JsonMoviesArrayKey);
        final String JSON_POSTER_PATH = context.getString(R.string.JsonMoviePosterPath);
        final String JSON_TITLE = context.getString(R.string.JsonMovieTitle);
        final String JSON_RELEASE_DATE = context.getString(R.string.JsonMovieReleaseDate);
        final String JSON_OVERVIEW = context.getString(R.string.JsonMovieOverview);
        final String JSON_VOTER_AVG = context.getString(R.string.JsonMovieVoteAvg);
        final String BASE_POSTER_URL = context.getString(R.string.BASE_MOVIE_POSTER_URL);
        final String JSON_MOVIE_ID = context.getString(R.string.JsonMovieId);

        ArrayList<Movie> movieList = new ArrayList<>();

//        use try/catch block to parse each object within the whole JSON object
        try {
            JSONObject moviesResultsJson = new JSONObject(movieJson);
            JSONArray moviesArray = moviesResultsJson.getJSONArray(MDB_RESULTS);

            for (int i = 0; i < moviesArray.length(); i++) {
                String posterPath;
                String title;
                String releaseDate;
                String overview;
                int movieId;
                double voteAvg;

                JSONObject currentMovie = moviesArray.getJSONObject(i);

                posterPath = currentMovie.getString(JSON_POSTER_PATH);
                title = currentMovie.getString(JSON_TITLE);
                releaseDate = currentMovie.getString(JSON_RELEASE_DATE);
                overview = currentMovie.getString(JSON_OVERVIEW);
                movieId = currentMovie.getInt(JSON_MOVIE_ID);
                voteAvg = currentMovie.getDouble(JSON_VOTER_AVG);
                String voteAvgString = String.valueOf(voteAvg);

//                concatenate base poster URL with poster path to create usable URL
                String posterUrl = BASE_POSTER_URL + posterPath;
                Log.v(TAG, context.getString(R.string.built_uri) + posterUrl);

//                add each JSON object into custom ArrayList<Movie> and add each movie into ArrayList
                Movie movie = new Movie(posterUrl, title, releaseDate, overview, movieId, voteAvgString);
                movieList.add(movie);
            }
        } catch (JSONException e) {
            Log.v(TAG, context.getString(R.string.parsing_problem), e);
            e.printStackTrace();
        }

        return movieList;
    }

    /**
     * Parses through JSON object of reviews from online database to create individual reviews to
     * be displayed in RecyclerView, appended to ArrayList<Review> for reviews on DetailsActivity.
     *
     * @param reviewJson JSON object from query of online database for movie reviews of specific
     *                   movies (by unique ID from online database).
     * @param context
     * @return ArrayList<Review> list of reviews to be returned
     */

    public static ArrayList<Review> extractJsonReviewData(String reviewJson, Context context) {

        String JSON_REVIEW_AUTHOR = context.getString(R.string.JsonReviewAuthor);
        String JSON_REVIEW_CONTENT = context.getString(R.string.JsonReviewContent);
        final String MDB_RESULTS = context.getString(R.string.JsonMoviesArrayKey);

        ArrayList<Review> reviewsList = new ArrayList<>();

        try {
            JSONObject reviewsResultJson = new JSONObject(reviewJson);
            JSONArray reviewsArray = reviewsResultJson.getJSONArray(MDB_RESULTS);

            for (int i = 0; i < reviewsArray.length(); i++) {
                String reviewAuthor;
                String reviewText;

                JSONObject currentReview = reviewsArray.getJSONObject(i);

                reviewAuthor = currentReview.getString(JSON_REVIEW_AUTHOR);
                reviewText = currentReview.getString(JSON_REVIEW_CONTENT);

                Review review = new Review(reviewAuthor, reviewText);
                reviewsList.add(review);
            }

        } catch (JSONException e) {
            Log.v(TAG, context.getString(R.string.parsing_problem), e);
            e.printStackTrace();
        }

        return reviewsList;
    }

    /**
     * Parses through JSON object to create individual trailers from online database.
     * Appends to ArrayList<Trailer> to be displayed in RecyclerView used for displaying trailer
     * thumbnails on DetailsActivity.
     *
     * @param trailerJson JSON object from query of online database for movie trailers of specific
     *                    movies (by unique ID from online database).
     * @param context
     * @return
     */

    public static ArrayList<Trailer> extractJsonTrailerData(String trailerJson, Context context) {

        String JSON_TRAILER_KEY = context.getString(R.string.JsonTrailerKey);
        String JSON_TRAILER_SITE = context.getString(R.string.JsonTrailerSite);
        String JSON_TRAILER_TYPE = context.getString(R.string.JsonTrailerType);
        final String MDB_RESULTS = context.getString(R.string.JsonMoviesArrayKey);
        String typeCompare = context.getString(R.string.JsonTrailerCompareType);
        String siteCompare = context.getString(R.string.JsonTrailerCompareSite);

        ArrayList<Trailer> trailerList = new ArrayList<>();

        try {
            JSONObject trailerResultJson = new JSONObject(trailerJson);
            JSONArray trailersArray = trailerResultJson.getJSONArray(MDB_RESULTS);

            for (int i = 0; i < trailersArray.length(); i++) {
                String trailerKey;
                String trailerSite;
                String trailerType;

                JSONObject currentTrailer = trailersArray.getJSONObject(i);

                trailerKey = currentTrailer.getString(JSON_TRAILER_KEY);
                trailerSite = currentTrailer.getString(JSON_TRAILER_SITE);
                trailerType = currentTrailer.getString(JSON_TRAILER_TYPE);

                if (trailerType.equals(typeCompare)) {

                    if (trailerSite.equals(siteCompare)) {
                        String trailerUrl = builtTrailerUrl(context, trailerKey);
                        String trailerThumbUrl = builtTrailerThumbUrl(context, trailerKey);

                        Log.v(TAG, "Trailer URL: " + trailerUrl);
                        Log.v(TAG, "Trailer Thumb: " + trailerThumbUrl);

                        Trailer trailer =
                                new Trailer(trailerKey, trailerSite, trailerType, trailerUrl, trailerThumbUrl);
                        trailerList.add(trailer);
                    }
                }

            }
        } catch (JSONException e) {
            Log.v(TAG, context.getString(R.string.parsing_problem), e);
            e.printStackTrace();
        }

        return trailerList;
    }

    /**
     * Builds the URL for the trailer of an individual movie that is parsed from JSON object in
     * method 'extractJsonTrailerData'.
     *
     * @param context
     * @param key unique movie YouTube key, parsed from JSON object
     * @return URL of trailer for individual movie
     */

    private static String builtTrailerUrl(Context context, String key) {

        String TRAILER_BASE_URL = context.getString(R.string.BASE_TRAILER_URL);
        String TRAILER_QUERY_KEY = context.getString(R.string.TRAILER_QUERY_KEY);

        Uri uri = Uri.parse(TRAILER_BASE_URL).buildUpon()
                .appendQueryParameter(TRAILER_QUERY_KEY, key)
                .build();

        URL url;
        String urlString = null;

        try {
            url = new URL(uri.toString());
            urlString = url.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlString;
    }

    /**
     * Builds the URL for the thumbnail for the trailer of an individual movie parsed from JSON
     * object in method 'extractJsonTrailerData'.
     *
     * @param context
     * @param key unique movie YouTube key, parsed from JSON object
     * @return URL of thumbnail of trailer
     */

    private static String builtTrailerThumbUrl(Context context, String key) {

        String THUMB_BASE_URL = context.getString(R.string.BASE_TRAILER_THUMB_URL);
        String THUMB_SUFFIX = context.getString(R.string.TRAILER_THUMB_SUFFIX);

        Uri uri = Uri.parse(THUMB_BASE_URL).buildUpon()
                .appendPath(key)
                .appendPath(THUMB_SUFFIX)
                .build();

        URL url;
        String urlString = null;

        try {
            url = new URL(uri.toString());
            urlString = url.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return urlString;
    }
}
