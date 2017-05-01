package com.example.android.movies.utilities;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.android.movies.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.net.ssl.HttpsURLConnection;

import static android.content.ContentValues.TAG;

/**
 * Created by Joseph Costlow on 18-Feb-17.
 */

public class NetworkUtils {

    private NetworkUtils() {
    }

    /**
     * Builds the URL necessary to query online database to retrieve top rated and most popular
     * lists.
     *
     * @param context
     * @param mSortOrderPath query parameter of online database (top rated or most popular)
     * @return URL of specific list (top rated or most popular)
     */

    public static URL builtMovieUrl(Context context, String mSortOrderPath) {

        String BASE_URL = context.getString(R.string.BASE_URL);
        String API_KEY_KEY = context.getString(R.string.API_KEY_KEY);
        String API_KEY_VALUE = context.getString(R.string.API_KEY_VALUE);
        String LANGUAGE_KEY = context.getString(R.string.LANGUAGE_KEY);
        String LANGUAGE_VALUE = context.getString(R.string.LANGUAGE_VALUE);

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(mSortOrderPath)
                .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_KEY, LANGUAGE_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, context.getString(R.string.built_uri) + builtUri);
        return url;
    }

    /**
     * Builds the URL necessary to retrieve reviews for an individual movie from online database.
     *
     * @param context
     * @param movieId unique movie ID from online database
     * @return URL of individual movie reviews list from online database
     */

    public static URL builtReviewUrl(Context context, int movieId) {

        String BASE_URL = context.getString(R.string.BASE_URL);
        String API_KEY_KEY = context.getString(R.string.API_KEY_KEY);
        String API_KEY_VALUE = context.getString(R.string.API_KEY_VALUE);
        String LANGUAGE_KEY = context.getString(R.string.LANGUAGE_KEY);
        String LANGUAGE_VALUE = context.getString(R.string.LANGUAGE_VALUE);
        String REVIEWS_PATH = context.getString(R.string.REVIEWS_PATH);
        String movieIdString = "" + movieId;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieIdString)
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_KEY, LANGUAGE_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, context.getString(R.string.built_uri) + builtUri);
        return url;
    }

    /**
     * Builds URL necessary to retrieve trailers for an individual movie from online database.
     *
     * @param context
     * @param movieId unique movie ID from online database
     * @return URL of individual movie trailers list from online database
     */

    public static URL builtTrailerUrl(Context context, int movieId) {

        String BASE_URL = context.getString(R.string.BASE_URL);
        String API_KEY_KEY = context.getString(R.string.API_KEY_KEY);
        String API_KEY_VALUE = context.getString(R.string.API_KEY_VALUE);
        String LANGUAGE_KEY = context.getString(R.string.LANGUAGE_KEY);
        String LANGUAGE_VALUE = context.getString(R.string.LANGUAGE_VALUE);
        String TRAILERS_PATH = context.getString(R.string.TRAILERS_PATH);
        String movieIdString = "" + movieId;

        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieIdString)
                .appendPath(TRAILERS_PATH)
                .appendQueryParameter(API_KEY_KEY, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_KEY, LANGUAGE_VALUE)
                .build();

        URL url = null;

        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, context.getString(R.string.built_uri) + builtUri);
        return url;
    }

    /**
     * Checks for a valid query of online database using URL built in method 'builtMovieUrl'.
     *
     * @param url URL of online database query for list (top rated or most popular)
     * @param context
     * @return JSON object to be parsed
     * @throws IOException
     */

    public static String getResponseFromHttpUrl(URL url, Context context) throws IOException {

        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;

//        set to GET method to retrieve JSON data
        try {
            urlConnection = (HttpsURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod(context.getString(R.string.REQUEST_METHOD));
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
//                read each line in JSON data and return
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(TAG, context.getString(R.string.error_response_code) + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, context.getString(R.string.retrieve_problem), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }

        return jsonResponse;
    }

    /**
     * Reads URL and creates JSON object to be parsed.
     *
     * @param inputStream
     * @return JSON object to be parsed
     * @throws IOException
     */

    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();

        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }
}