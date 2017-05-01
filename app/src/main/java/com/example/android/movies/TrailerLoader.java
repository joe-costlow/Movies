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
 * Created by Joseph Costlow on 20-Mar-17.
 */

public class TrailerLoader extends AsyncTaskLoader<ArrayList<Trailer>> implements LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

    private static final String TAG = TrailerLoader.class.getSimpleName();

    private Context mContext;
    private TrailerAdapter mAdapter;
    private ArrayList<Trailer> mTrailerList;
    private ArrayList<Trailer> mData;
    private int mMovieId;
    private String trailerJsonResponse;
    private RecyclerView mTrailerRecyclerView;
    private TextView mEmptyTrailers;

    public TrailerLoader(Context context, int movieId, TrailerAdapter adapter, RecyclerView trailerRecyclerView, TextView emptyTrailers) {
        super(context);
        mContext = context;
        mMovieId = movieId;
        mAdapter = adapter;
        mTrailerRecyclerView = trailerRecyclerView;
        mEmptyTrailers = emptyTrailers;
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
    public void deliverResult(ArrayList<Trailer> data) {

        mData = data;
        super.deliverResult(data);
    }

    @Override
    public ArrayList<Trailer> loadInBackground() {

        mTrailerList = null;
        URL builtTrailerUrl = NetworkUtils.builtTrailerUrl(mContext, mMovieId);

        try {
            trailerJsonResponse = NetworkUtils.getResponseFromHttpUrl(builtTrailerUrl, mContext);
            mTrailerList = JsonUtils.extractJsonTrailerData(trailerJsonResponse, mContext);
        } catch (IOException e) {
            Log.v(TAG, mContext.getString(R.string.http_error), e);
            e.printStackTrace();
        }

        return mTrailerList;
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int loaderId, Bundle bundle) {

        return new TrailerLoader(mContext, mMovieId, mAdapter, mTrailerRecyclerView, mEmptyTrailers);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> trailers) {

        boolean connChecker = connCheck();

        if (connChecker) {
            showTrailers();
            mAdapter.swap(trailers);
        } else {
            showEmptyView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        loader.reset();
    }

    private boolean connCheck() {

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void showTrailers() {
        mTrailerRecyclerView.setVisibility(View.VISIBLE);
        mEmptyTrailers.setVisibility(View.GONE);
    }

    private void showEmptyView() {
        mTrailerRecyclerView.setVisibility(View.GONE);
        mEmptyTrailers.setVisibility(View.VISIBLE);
        mEmptyTrailers.setText(mContext.getString(R.string.no_trailers));
    }
}
