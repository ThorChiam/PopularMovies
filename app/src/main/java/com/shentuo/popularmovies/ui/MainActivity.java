package com.shentuo.popularmovies.ui;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.global.Constants;
import com.shentuo.popularmovies.model.Poster;
import com.shentuo.popularmovies.ui.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ListItemClickListener {
    private MoviesAdapter mAdapter;
    private RecyclerView mMoviesList;
    private final int MOST_POPULAR = 1;
    private final int TOP_RATED = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mMoviesList = (RecyclerView) findViewById(R.id.rv_movies);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesList.setLayoutManager(layoutManager);
        mMoviesList.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //Auto adjust the column number of grid view
                mMoviesList.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int viewWidth = mMoviesList.getMeasuredWidth();
                Display display = getWindowManager().getDefaultDisplay();
                DisplayMetrics outMetrics = new DisplayMetrics();
                display.getMetrics(outMetrics);

                float density = getResources().getDisplayMetrics().density;
                float gridViewWidth = getResources().getDimension(R.dimen.gridview_layout_width) / density;
                int newSpanCount = (int) Math.floor(viewWidth / gridViewWidth);
                layoutManager.setSpanCount(newSpanCount);
                layoutManager.requestLayout();
            }
        });

        mAdapter = new MoviesAdapter(this);

        mMoviesList.setAdapter(mAdapter);

        //Show most popular movies by default
        getMoviePosters(MOST_POPULAR);
    }

    private void getMoviePosters(int sortType) {
        URL requestUrl;
        if (sortType == MOST_POPULAR) {
            requestUrl = NetworkUtils.buildUrlForMostPopular();
        } else {
            requestUrl = NetworkUtils.buildUrlForTopRated();
        }

        new GetMoviePostersTask().execute(requestUrl);
    }

    public class GetMoviePostersTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String getMoviesResults = null;
            try {
                getMoviesResults = NetworkUtils.getResponseFromHttpUrl(searchUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return getMoviesResults;
        }

        @Override
        protected void onPostExecute(String getMoviesResults) {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.most_popular) {
            getMoviePosters(MOST_POPULAR);
            return true;
        } else if (id == R.id.top_rated) {
            getMoviePosters(TOP_RATED);
            return true;
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
