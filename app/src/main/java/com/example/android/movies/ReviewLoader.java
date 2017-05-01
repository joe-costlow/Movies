package com.example.android.movies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.movies.utilities.JsonUtils;
import com.example.android.movies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Joseph Costlow on 19-Mar-17.
 */

public class ReviewLoader extends AsyncTaskLoader<ArrayList<Review>> implements LoaderManager.LoaderCallbacks<ArrayList<Review>> {

    private static final String TAG = ReviewLoader.class.getSimpleName();
    private Context mContext;
    private ReviewAdapter mAdapter;
    private ArrayList<Review> mData;
    private ArrayList<Review> reviewList;
    private String reviewJsonResponse;
    private int mMovieId;
    private RecyclerView mReviewRecyclerView;
    private TextView mEmptyReviews;

    public ReviewLoader(Context context, int movieId, ReviewAdapter adapter, RecyclerView reviewRecyclerView, TextView emptyReviews) {
        super(context);
        mContext = context;
        mMovieId = movieId;
        mAdapter = adapter;
        mReviewRecyclerView = reviewRecyclerView;
        mEmptyReviews = emptyReviews;
    }

    @Override
    protected void onStartLoading() {

        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(ArrayList<Review> data) {
        mData = data;
        super.deliverResult(data);
    }

    @Override
    public ArrayList<Review> loadInBackground() {

//        create ArrayList<Review>, set to null, set to returned data
        reviewList = null;
        URL builtReviewUrl = NetworkUtils.builtReviewUrl(mContext, mMovieId);

        try {
            reviewJsonResponse = NetworkUtils.getResponseFromHttpUrl(builtReviewUrl, mContext);
            reviewList = JsonUtils.extractJsonReviewData(reviewJsonResponse, mContext);
        } catch (IOException e) {
            Log.v(TAG, mContext.getString(R.string.http_error), e);
            e.printStackTrace();
        }

        return reviewList;
    }

    @Override
    public Loader<ArrayList<Review>> onCreateLoader(int loaderId, Bundle bundle) {

        return new ReviewLoader(mContext, mMovieId, mAdapter, mReviewRecyclerView, mEmptyReviews);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> reviews) {

        boolean connChecker = connCheck();

        if (connChecker) {
            showReviews();
            mAdapter.swap(reviews);
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        loader.reset();
    }

    private boolean connCheck() {

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void showReviews() {
        mReviewRecyclerView.setVisibility(View.VISIBLE);
        mEmptyReviews.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        mReviewRecyclerView.setVisibility(View.GONE);
        mEmptyReviews.setVisibility(View.VISIBLE);
        mEmptyReviews.setText(mContext.getString(R.string.no_reviews));
    }

}
