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
import android.widget.Toast;

import com.example.android.movies.utilities.FavoritesQuery;
import com.example.android.movies.utilities.JsonUtils;
import com.example.android.movies.utilities.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by Joseph Costlow on 18-Feb-17.
 */

public class MovieLoader extends AsyncTaskLoader<ArrayList<Movie>> implements LoaderManager.LoaderCallbacks<ArrayList<Movie>> {

    private static final String TAG = MovieLoader.class.getSimpleName();
    private String mSortOrderPath;
    private Context mContext;
    private MovieAdapter mAdapter;
    private ArrayList<Movie> mData;
    private String movieJsonResponse;
    private RecyclerView mRecyclerView;
    private TextView mEmptyState;

    public MovieLoader(Context context, String sortById, MovieAdapter adapter, RecyclerView recyclerView, TextView emptyState) {
        super(context);
        mSortOrderPath = sortById;
        mContext = context;
        mAdapter = adapter;
        mRecyclerView = recyclerView;
        mEmptyState = emptyState;
    }

    @Override
    protected void onStartLoading() {

        showGrid();

        if (mSortOrderPath.equals(mContext.getString(R.string.SORT_BY_FAVORITES_PATH))) {
            forceLoad();
        }

        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(ArrayList<Movie> data) {

        mData = data;
        super.deliverResult(data);
    }

    @Override
    public ArrayList<Movie> loadInBackground() {

//        create ArrayList<Movie>, set to null, set to returned data
        ArrayList<Movie> movieList = null;
        URL builtMovieUrl = NetworkUtils.builtMovieUrl(mContext, mSortOrderPath);

        if (mSortOrderPath.equals(mContext.getString(R.string.SORT_BY_TOP_RATED_PATH))
                || mSortOrderPath.equals(mContext.getString(R.string.SORT_BY_MOST_POPULAR_PATH))) {
            try {
                movieJsonResponse = NetworkUtils.getResponseFromHttpUrl(builtMovieUrl, mContext);
                movieList = JsonUtils.extractMovieDataFromJson(movieJsonResponse, mContext);
            } catch (IOException e) {
                Log.v(TAG, mContext.getString(R.string.http_error), e);
                e.printStackTrace();
            }
        } else {
            movieList = FavoritesQuery.fetchFavorites(mContext);
        }

        return movieList;
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int loaderId, Bundle bundle) {

        return new MovieLoader(mContext, mSortOrderPath, mAdapter, mRecyclerView, mEmptyState);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {

        boolean connChecker = connCheck();
        int loaderId = loader.getId();

        switch (loaderId) {

            case MainActivity.MAIN_MOVIES_LIST_LOADER:
                if (connChecker) {
                    showGrid();
                } else {
                    showEmptyView();
                }
                break;

            case MainActivity.MOST_POPULAR_LIST_LOADER:
                if (connChecker) {
                    showGrid();
                } else {
                    showEmptyView();
                }
                break;

            case MainActivity.FAVORITES_LOADER:
                if (!connChecker) {

                    if (MainActivity.mToast != null) MainActivity.mToast.cancel();

                    MainActivity.mToast = Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT);
                    MainActivity.mToast.show();
                }

                if (movies.isEmpty()) {
                    showEmptyView();
                    mEmptyState.setText(mContext.getString(R.string.no_favorites));
                } else {
                    showGrid();
                }
                break;
        }

            mAdapter.swap(movies);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

        loader.reset();
    }

    private boolean connCheck() {

        ConnectivityManager connMgr = (ConnectivityManager) mContext.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        return networkInfo != null && networkInfo.isConnected();
    }

    private void showGrid() {

        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyState.setVisibility(View.GONE);
    }

    private void showEmptyView() {

        mRecyclerView.setVisibility(View.GONE);
        mEmptyState.setVisibility(View.VISIBLE);
    }
}