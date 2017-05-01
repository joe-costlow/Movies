package com.example.android.movies;

import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.movies.utilities.AutofitGrid;
import com.example.android.movies.utilities.GridItemDecoration;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int MAIN_MOVIES_LIST_LOADER = 1;
    public static final int MOST_POPULAR_LIST_LOADER = 2;
    public static final int FAVORITES_LOADER = 3;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static Toast mToast;
    public int loaderId;
    TextView mEmptyState;
    SharedPreferences loadingPref;
    private String sortOrderPathKey;
    private String mainActivitySortLabelKey;
    private String loaderIdKey;
    private String topRatedLabel;
    private String mostPopularLabel;
    private String favoritesLabel;
    private String appName;
    private String sortLabel;
    private boolean connChecker;
    private int mNumberOfColumns;
    private String mSortOrderPath;
    private MovieAdapter mAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private String sharedPrefSortOrderKey;
    private String sharedPrefSortLabelKey;
    private String sharedPrefLoaderKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyState = (TextView) findViewById(R.id.empty_view);
        recyclerView = (RecyclerView) findViewById(R.id.rv_movie_list);

//        keys used in onSaveInstanceState for default sort list displayed at startup (top rated)
        sortOrderPathKey = getString(R.string.oss_sort_order_path_key);
        mainActivitySortLabelKey = getString(R.string.oss_main_activity_sort_label_key);
        loaderIdKey = getString(R.string.oss_loader_id_key);

//        keys used in SharedPreferences for list to display at startup
        sharedPrefSortOrderKey = getString(R.string.SHARED_PREF_SORT_KEY);
        sharedPrefSortLabelKey = getString(R.string.SHARED_PREF_LABEL_KEY);
        sharedPrefLoaderKey = getString(R.string.SHARED_PREF_LOADER_KEY);

//        labels for Main Activity
        topRatedLabel = getString(R.string.activity_main_label_top_rated);
        mostPopularLabel = getString(R.string.activity_main_label_most_popular);
        favoritesLabel = getString(R.string.activity_main_label_favorites);
        appName = getString(R.string.app_name);

//        check to see if a value for sorting list is saved
        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(sortOrderPathKey)
                    && savedInstanceState.containsKey(mainActivitySortLabelKey)
                    && savedInstanceState.containsKey(loaderIdKey)) {
//                set list and label to saved information
                mSortOrderPath = savedInstanceState.getString(sortOrderPathKey);
                sortLabel = savedInstanceState.getString(mainActivitySortLabelKey);
                loaderId = savedInstanceState.getInt(loaderIdKey);
            }

        } else {

//            check SharedPreferences for saved list information
            loadingPref = getPreferences(0);

            if (loadingPref != null) {
//                set list information to saved information
                mSortOrderPath = loadingPref.getString(sharedPrefSortOrderKey, getString(R.string.SORT_BY_TOP_RATED_PATH));
                sortLabel = loadingPref.getString(sharedPrefSortLabelKey, topRatedLabel);
                loaderId = loadingPref.getInt(sharedPrefLoaderKey, MAIN_MOVIES_LIST_LOADER);

            } else {

//                use default values for sort order (top rated) and activity label
                mSortOrderPath = getString(R.string.SORT_BY_TOP_RATED_PATH);
                sortLabel = topRatedLabel;
                loaderId = MAIN_MOVIES_LIST_LOADER;
            }
        }

        setTitle(appName + sortLabel);

//        all list items are the same size
        recyclerView.setHasFixedSize(true);

//        use decoration to remove margins between rows and columns in GridLayoutManager
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.grid_spacing_height);
        recyclerView.addItemDecoration(new GridItemDecoration(this, spacingInPixels));

//        determine how many columns can fit for each orientation
        mNumberOfColumns = AutofitGrid.calcNumberOfColumns(getApplicationContext());
        layoutManager = new GridLayoutManager(this, mNumberOfColumns);
        recyclerView.setLayoutManager(layoutManager);

//        set adapter to recycler view
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());
        recyclerView.setAdapter(mAdapter);

        showEmptyView();

//        check for internet connection before attempting to get data
        connChecker = connCheck();

//        initialize loader determined earlier
        getLoaderManager().initLoader(loaderId, null, new MovieLoader(this, mSortOrderPath, mAdapter, recyclerView, mEmptyState));

//        check for network connectivity and show either list or an empty view. show favorites if
//        there is no network connectivity
        if (connChecker) {
            showGridView();
        } else {
            showEmptyView();
            if (mSortOrderPath.equals(getString(R.string.SORT_BY_FAVORITES_PATH))) {

//                show toast indicating no network connectivity
                if (mToast != null) mToast.cancel();

                mToast = Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT);
                mToast.show();
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mSortOrderPath = savedInstanceState.getString(sortOrderPathKey);
        sortLabel = savedInstanceState.getString(mainActivitySortLabelKey);
        loaderId = savedInstanceState.getInt(loaderIdKey);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

//        save sort order and matching activity label
        outState.putString(sortOrderPathKey, mSortOrderPath);
        outState.putString(mainActivitySortLabelKey, sortLabel);
        outState.putInt(loaderIdKey, loaderId);
    }

    @Override
    protected void onPause() {

//        save list information to SharedPreferences
        loadingPref = getPreferences(0);
        SharedPreferences.Editor editor = loadingPref.edit();
        editor.putString(sharedPrefSortOrderKey, mSortOrderPath);
        editor.putString(sharedPrefSortLabelKey, sortLabel);
        editor.putInt(sharedPrefLoaderKey, loaderId);
        editor.commit();

        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

//            top rated list selected
            case R.id.action_top_rated:
//                set loader to show top rated
                loaderId = MAIN_MOVIES_LIST_LOADER;

//                create label for activity
                sortLabel = topRatedLabel;
                setTitle(appName + sortLabel);

//                set sort order to top rated
                mSortOrderPath = getString(R.string.SORT_BY_TOP_RATED_PATH);
                showEmptyView();

//                check for internet connection
                connChecker = connCheck();

                if (connChecker) {
                    showGridView();
                    getLoaderManager().restartLoader(MAIN_MOVIES_LIST_LOADER, null, new MovieLoader(this, mSortOrderPath, mAdapter, recyclerView, mEmptyState));
                } else {
                    showEmptyView();
                    mEmptyState.setText(getString(R.string.no_internet));
                }

                return true;

//            most popular list selected
            case R.id.action_most_popular:
//                set loader to show most popular
                loaderId = MOST_POPULAR_LIST_LOADER;

//                create label for main activity
                sortLabel = mostPopularLabel;
                setTitle(appName + sortLabel);

//                set sort order to most popular
                mSortOrderPath = getString(R.string.SORT_BY_MOST_POPULAR_PATH);
                showEmptyView();

//                check for internet connection
                connChecker = connCheck();

                if (connChecker) {
                    showGridView();
                    getLoaderManager().restartLoader(MOST_POPULAR_LIST_LOADER, null, new MovieLoader(this, mSortOrderPath, mAdapter, recyclerView, mEmptyState));
                } else {
                    showEmptyView();
                    mEmptyState.setText(getString(R.string.no_internet));
                }

                return true;

//            favorites list is selected
            case R.id.action_favorites:
//                set loader to show favorites
                loaderId = FAVORITES_LOADER;
                showGridView();
                mSortOrderPath = getString(R.string.SORT_BY_FAVORITES_PATH);
                sortLabel = favoritesLabel;
                setTitle(appName + sortLabel);

                if (!connCheck()) {

                    if (mToast != null) mToast.cancel();

                    mToast = Toast.makeText(this, getString(R.string.no_internet), Toast.LENGTH_SHORT);
                    mToast.show();
                }

                getLoaderManager().restartLoader(FAVORITES_LOADER, null, new MovieLoader(this, mSortOrderPath, mAdapter, recyclerView, mEmptyState));

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    check for network connectivity and set empty view
    private boolean connCheck() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            showGridView();
            return true;
        } else {
            showEmptyView();
            mEmptyState.setText(R.string.no_internet);
            return false;
        }
    }

    public void showGridView() {

//        make list viewable
        mEmptyState.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    public void showEmptyView() {

//        make empty state view visible
        mEmptyState.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }
}