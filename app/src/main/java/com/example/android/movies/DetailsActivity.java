package com.example.android.movies;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.data.MovieContract;
import com.example.android.movies.utilities.FavoritesQuery;
import com.example.android.movies.utilities.GridItemDecoration;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    public static final int REVIEWS_LOADER_ID = 11;
    public static final int TRAILERS_LOADER_ID = 12;
    private static final String TAG = DetailsActivity.class.getSimpleName();
    public RecyclerView mReviewRecyclerView;
    int currentMoviePosition;
    String currentMoviePoster;
    String currentMovieTitle;
    String currentMovieReleaseDate;
    String currentMovieOverview;
    String currentMovieRating;
    int currentMovieId;
    boolean isFavorite = false;
    Context context;
    private ImageView mPoster;
    private TextView mTitle;
    private TextView mReleaseDate;
    private TextView mOverview;
    private TextView mRating;
    private TextView mFavoriteButtonTextView;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView.LayoutManager mReviewLayoutManager;

    private RecyclerView mTrailerRecyclerView;
    private TrailerAdapter mTrailerAdapter;
    private RecyclerView.LayoutManager mTrailerLayoutManager;

    private TextView mEmptyTrailers;
    private TextView mEmptyReviews;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

//        create an intent to receive from Main Activity
        Intent intent = getIntent();

//        get extras sent from onClick in MovieAdapter
        currentMoviePosition = intent.getIntExtra(getString(R.string.intent_to_details_position_key), 0);
        currentMoviePoster = intent.getStringExtra(getString(R.string.intent_to_details_poster_key));
        currentMovieTitle = intent.getStringExtra(getString(R.string.intent_to_details_title_key));
        currentMovieReleaseDate = intent.getStringExtra(getString(R.string.intent_to_details_release_date_key));
        currentMovieOverview = intent.getStringExtra(getString(R.string.intent_to_details_overview_key));
        currentMovieRating = intent.getStringExtra(getString(R.string.intent_to_details_rating_key));
        currentMovieId = intent.getIntExtra(getString(R.string.intent_to_details_id_key), 0);

//        find the Views used to display movie information
        mPoster = (ImageView) findViewById(R.id.movie_poster);
        mTitle = (TextView) findViewById(R.id.movie_title);
        mReleaseDate = (TextView) findViewById(R.id.release_date);
        mOverview = (TextView) findViewById(R.id.overview);
        mRating = (TextView) findViewById(R.id.vote_avg);
        mReviewRecyclerView = (RecyclerView) findViewById(R.id.rv_review_list);
        mTrailerRecyclerView = (RecyclerView) findViewById(R.id.rv_trailer_list);
        mFavoriteButtonTextView = (TextView) findViewById(R.id.tv_favorite_button);

        mEmptyTrailers = (TextView) findViewById(R.id.tv_empty_trailers);
        mEmptyTrailers.setVisibility(View.GONE);

        mEmptyReviews = (TextView) findViewById(R.id.tv_empty_reviews);
        mEmptyReviews.setVisibility(View.GONE);

//        loads image of individual movie to RecyclerView
        Picasso.with(this).load(currentMoviePoster).into(mPoster);

//        set values from intent extras to Views
        mTitle.setText(currentMovieTitle);
        mReleaseDate.setText(currentMovieReleaseDate);
        mOverview.setText(currentMovieOverview);
        mRating.setText(currentMovieRating);

//        Determines if displayed movie is in the Favorites database
        isFavorite = FavoritesQuery.compareFavoritesMovieId(this, currentMovieId);

//        If a movie is a favorite, set text of 'button' appropriately
        if (!isFavorite) {
            mFavoriteButtonTextView.setText(R.string.button_text_add_favorite);
        } else {
            mFavoriteButtonTextView.setText(R.string.button_text_remove_favorite);
        }

//        set up vertical layout for reviews of current movie
        mReviewLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mReviewRecyclerView.setLayoutManager(mReviewLayoutManager);

//        set up horizontal layout for trailers of current movie
        mTrailerLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mTrailerRecyclerView.setLayoutManager(mTrailerLayoutManager);

//        set adapter of RecyclerView used to display reviews of current movie
        mReviewAdapter = new ReviewAdapter(this, new ArrayList<Review>());
        mReviewRecyclerView.setAdapter(mReviewAdapter);

//        set adapter of RecyclerView used to display trailers of current movie
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.trailer_spacing);
        mTrailerRecyclerView.addItemDecoration(new GridItemDecoration(context, spacingInPixels));
        mTrailerAdapter = new TrailerAdapter(this, new ArrayList<Trailer>());
        mTrailerRecyclerView.setAdapter(mTrailerAdapter);

//        initialize loaders for the trailers and reviews of current movie
        getLoaderManager().initLoader(REVIEWS_LOADER_ID, null, new ReviewLoader(this, currentMovieId, mReviewAdapter, mReviewRecyclerView, mEmptyReviews));
        getLoaderManager().initLoader(TRAILERS_LOADER_ID, null, new TrailerLoader(this, currentMovieId, mTrailerAdapter, mTrailerRecyclerView, mEmptyTrailers));

//        adds or removes current movie to/from Favorites database table
        mFavoriteButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                determines if current movie is already a favorite
                isFavorite = FavoritesQuery.compareFavoritesMovieId(getApplicationContext(), currentMovieId);

                if (isFavorite) {

//                    find the current movie in the Favorites table by the unique movie ID from online
//                    database, returning _ID of current movie
                    long currentMovieDbId = FavoritesQuery.favoriteToBeDeleted(getApplicationContext(), currentMovieId);
                    Uri currentMovieUri = ContentUris.withAppendedId(MovieContract.FavoritesEntry.CONTENT_URI_FAVORITES, currentMovieDbId);
                    Log.v(TAG, "Uri built to delete: " + currentMovieUri);

                    int rowsDeleted = getContentResolver().delete(currentMovieUri, null, null);

//                    set text of 'button' to make movie favorite
                    mFavoriteButtonTextView.setText(R.string.button_text_add_favorite);

//                    display toast of removal of current movie from Favorites table
                    if (MainActivity.mToast != null) MainActivity.mToast.cancel();

                    MainActivity.mToast = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.favorite_removed), Toast.LENGTH_SHORT);
                    MainActivity.mToast.show();

                } else {

//                    write current movie information to Favorites table
                    ContentValues contentValues = new ContentValues();
                    contentValues.clear();
                    contentValues.put(MovieContract.FavoritesEntry.COLUMN_POSTER_PATH, currentMoviePoster);
                    contentValues.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_TITLE, currentMovieTitle);
                    contentValues.put(MovieContract.FavoritesEntry.COLUMN_OVERVIEW, currentMovieOverview);
                    contentValues.put(MovieContract.FavoritesEntry.COLUMN_RELEASE_DATE, currentMovieReleaseDate);
                    contentValues.put(MovieContract.FavoritesEntry.COLUMN_VOTE_AVG, currentMovieRating);
                    contentValues.put(MovieContract.FavoritesEntry.COLUMN_MOVIE_ID, currentMovieId);
                    Uri uri = getContentResolver().insert(MovieContract.FavoritesEntry.CONTENT_URI_FAVORITES, contentValues);

//                    set text of 'button' to remove from favorites
                    mFavoriteButtonTextView.setText(R.string.button_text_remove_favorite);

//                    display toast of current movie added to Favorites table
                    if (MainActivity.mToast != null) MainActivity.mToast.cancel();

                    MainActivity.mToast = Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.favorite_added), Toast.LENGTH_SHORT);
                    MainActivity.mToast.show();
                }
            }
        });


    }
}
