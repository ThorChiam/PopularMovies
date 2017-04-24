package com.shentuo.popularmovies.ui;

import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.data.MovieListContract;
import com.shentuo.popularmovies.databinding.ActivityMainBinding;
import com.shentuo.popularmovies.global.Constants;
import com.shentuo.popularmovies.model.Poster;
import com.shentuo.popularmovies.ui.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener,
        LoaderManager.LoaderCallbacks<String> {
    private static final String TAG = "MovieDetailActivity";
    private MoviesAdapter mAdapter;
    private RecyclerView mMoviesList;
    private final int MOST_POPULAR = 1;
    private final int TOP_RATED = 2;
    private final int FAVORITES = 3;
    private static final String GET_MOVIE_QUERY_URL = "getMovies";
    private static final int GET_MOVIE_LOADER = 22;
    private ProgressBar mLoadingIndicator;
    private ActivityMainBinding mBinding;
    private List<Poster> favoriteList;
    private int selectedItem = MOST_POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);

        mMoviesList = mBinding.rvMovies;
        mLoadingIndicator = mBinding.pbLoadingIndicator;
        final GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mMoviesList.setLayoutManager(layoutManager);

        mAdapter = new MoviesAdapter(this);

        mMoviesList.setAdapter(mAdapter);

        favoriteList = new ArrayList<>();
        //Show most popular movies by default
        if (NetworkUtils.isOnline(this)) {
            selectedItem = MOST_POPULAR;
            getMoviePosters(MOST_POPULAR);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (favoriteList == null) {
            favoriteList = new ArrayList<>();
        }
        if (selectedItem == FAVORITES) {
            populateFavorites();
        } else {
            getMoviePosters(selectedItem);
        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int width = displayMetrics.widthPixels;
        int nColumns = width / Constants.WIDTH_DIVIDER;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    private void getMoviePosters(int sortType) {
        URL requestUrl;
        getAllFavorites();
        if (sortType == MOST_POPULAR) {
            requestUrl = NetworkUtils.buildUrlForMostPopular();
        } else {
            requestUrl = NetworkUtils.buildUrlForTopRated();
        }

        Bundle queryBundle = new Bundle();

        queryBundle.putString(GET_MOVIE_QUERY_URL, requestUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> getMovieLoader = loaderManager.getLoader(GET_MOVIE_LOADER);

        if (getMovieLoader == null) {
            loaderManager.initLoader(GET_MOVIE_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(GET_MOVIE_LOADER, queryBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);

                forceLoad();
            }

            @Override
            public String loadInBackground() {
                String getMovieUrlString = args.getString(GET_MOVIE_QUERY_URL);

                if (getMovieUrlString == null) {
                    return null;
                }

                try {
                    URL getMovieUrl = new URL(getMovieUrlString);
                    return NetworkUtils.getResponseFromHttpUrl(getMovieUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String getMoviesResults) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        if (getMoviesResults != null && !getMoviesResults.equals("")) {
            try {
                JSONObject response = new JSONObject(getMoviesResults);
                JSONArray results = response.getJSONArray(Constants.RESULT_KEY);
                mAdapter.clearItems();
                for (int i = 0; i < results.length(); i++) {
                    Poster poster = new Poster(results.getJSONObject(i));
                    poster.setFavorited(isInFavoriteList(poster.getId()));
                    mAdapter.addPosterItem(poster);
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isInFavoriteList(int movieId) {
        for (Poster poster : favoriteList) {
            if (poster.getId() == movieId) {
                return true;
            }
        }
        return false;
    }

    private void populateFavorites() {
        getAllFavorites();

        if (favoriteList != null && !favoriteList.isEmpty()) {
            mAdapter.clearItems();
            for (Poster poster : favoriteList) {
                poster.setFavorited(true);
                mAdapter.addPosterItem(poster);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (NetworkUtils.isOnline(this)) {
            if (id == R.id.most_popular) {
                selectedItem = MOST_POPULAR;
                getMoviePosters(MOST_POPULAR);
                return true;
            } else if (id == R.id.top_rated) {
                selectedItem = TOP_RATED;
                getMoviePosters(TOP_RATED);
                return true;
            } else if (id == R.id.favorites) {
                selectedItem = FAVORITES;
                populateFavorites();
            }
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListItemClick(String jsonString) {
        Intent startDetailIntent = new Intent(MainActivity.this, MovieDetailActivity.class);
        startDetailIntent.putExtra(Constants.EXTRA_KEY, jsonString);
        startActivity(startDetailIntent);
    }

    private void getAllFavorites() {
        Cursor mCursor = getContentResolver().query(MovieListContract.MovieListEntry.CONTENT_URI, null, null, null, null);
        if (mCursor != null && mCursor.getCount() > 0) {
            favoriteList.clear();
            for (int i = 0; i < mCursor.getCount(); i++) {
                mCursor.moveToPosition(i);
                Poster poster = new Poster();
                poster.setFavorited(true);
                poster.setId(mCursor.getInt(mCursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_ID)));
                poster.setTitle(mCursor.getString(mCursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_TITLE)));
                poster.setPoster_path(mCursor.getString(mCursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_POSTER_PATH)));
                poster.setOverview(mCursor.getString(mCursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_OVERVIEW)));
                poster.setVote_average(mCursor.getDouble(mCursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_VOTE_AVERAGE)));
                poster.setRelease_date(mCursor.getString(mCursor.getColumnIndex(MovieListContract.MovieListEntry.COLUMN_RELEASE_DATE)));
                favoriteList.add(poster);
            }
        }
        if (mCursor != null) {
            mCursor.close();
        }
    }
}
