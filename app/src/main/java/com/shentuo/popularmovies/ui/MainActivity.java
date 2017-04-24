package com.shentuo.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.global.Constants;
import com.shentuo.popularmovies.model.Poster;
import com.shentuo.popularmovies.ui.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<String> {
    private MoviesAdapter mAdapter;
    private RecyclerView mMoviesList;
    private final int MOST_POPULAR = 1;
    private final int TOP_RATED = 2;
    private static final String GET_MOVIE_QUERY_URL = "getMovies";
    private static final int GET_MOVIE_LOADER = 22;
    private ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns());
        mMoviesList.setLayoutManager(layoutManager);

        mAdapter = new MoviesAdapter(this);

        mMoviesList.setAdapter(mAdapter);

        //Show most popular movies by default
        if (NetworkUtils.isOnline(this)) {
            getMoviePosters(MOST_POPULAR);
        } else {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
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
                    mAdapter.addPosterItem(new Poster(results.getJSONObject(i)));
                }
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                getMoviePosters(MOST_POPULAR);
                return true;
            } else if (id == R.id.top_rated) {
                getMoviePosters(TOP_RATED);
                return true;
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
}
