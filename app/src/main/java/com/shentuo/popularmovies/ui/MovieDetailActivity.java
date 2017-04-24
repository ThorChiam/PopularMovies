package com.shentuo.popularmovies.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.global.Constants;
import com.shentuo.popularmovies.model.Poster;
import com.shentuo.popularmovies.model.Review;
import com.shentuo.popularmovies.model.Trailer;
import com.shentuo.popularmovies.ui.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShentuoZhan on 13/3/17.
 */

public class MovieDetailActivity extends AppCompatActivity implements TrailersAdapter.ListItemClickListener, LoaderManager.LoaderCallbacks<String> {
    private Poster poster;
    private TrailersAdapter mTrailerAdapter;
    private RecyclerView mTrailersList;
    private ReviewsAdapter mReviewAdapter;
    private RecyclerView mReviewsList;
    private static final String GET_TRAILERS_URL = "getTrailers";
    private static final int GET_TRAILERS_LOADER = 23;
    private static final String GET_REVIEWS_URL = "getReviews";
    private static final int GET_REVIEWS_LOADER = 24;
    private static final String TAG = "MovieDetailActivity";
    @BindView(R.id.movie_title)
    TextView tvTitle;
    @BindView(R.id.movie_thumbnail)
    ImageView ivPoster;
    @BindView(R.id.movie_overview)
    TextView tvOverview;
    @BindView(R.id.user_rating)
    TextView tvRating;
    @BindView(R.id.release_date)
    TextView tvReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_KEY)) {
            String jsonString = intent.getStringExtra(Constants.EXTRA_KEY);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                poster = new Poster(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        mTrailersList = (RecyclerView) findViewById(R.id.rv_trailers);
        mReviewsList = (RecyclerView) findViewById(R.id.rv_reviews);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        final GridLayoutManager layoutManager2 = new GridLayoutManager(this, 1);
        mTrailersList.setLayoutManager(layoutManager);
        mReviewsList.setLayoutManager(layoutManager2);
        mTrailerAdapter = new TrailersAdapter(this);
        mReviewAdapter = new ReviewsAdapter();

        mTrailersList.setAdapter(mTrailerAdapter);
        mReviewsList.setAdapter(mReviewAdapter);

        if (poster != null) {
            tvTitle.setText(poster.getOriginal_title());
            String imageURL = Constants.BASE_IMAGE_URL + Constants.THUMBNAIL_SIZE + "/" + poster.getPoster_path();
            Picasso.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.ic_picture)
                    .error(R.drawable.ic_error)
                    .into(ivPoster);
            tvOverview.setText(poster.getOverview());
            String userRate = getResources().getString(R.string.user_rate) + poster.getVote_average();
            tvRating.setText(userRate);
            String releaseDate = getResources().getString(R.string.release_date) + poster.getRelease_date();
            tvReleaseDate.setText(releaseDate);

            if (NetworkUtils.isOnline(this)) {
                getTrailersAndReviews();
            } else {
                Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
            }
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void getTrailersAndReviews() {
        URL getTrailersUrl = NetworkUtils.buildUrlForTrailers(poster.getId());
        URL getReviewsUrl = NetworkUtils.buildUrlForReviews(poster.getId());

        Bundle queryBundle = new Bundle();

        queryBundle.putString(GET_TRAILERS_URL, getTrailersUrl.toString());
        queryBundle.putString(GET_REVIEWS_URL, getReviewsUrl.toString());

        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> getTrailerLoader = loaderManager.getLoader(GET_TRAILERS_LOADER);
        Loader<String> getReviewLoader = loaderManager.getLoader(GET_REVIEWS_LOADER);

        if (getTrailerLoader == null) {
            loaderManager.initLoader(GET_TRAILERS_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(GET_TRAILERS_LOADER, queryBundle, this);
        }

        if (getReviewLoader == null) {
            loaderManager.initLoader(GET_REVIEWS_LOADER, queryBundle, this);
        } else {
            loaderManager.restartLoader(GET_REVIEWS_LOADER, queryBundle, this);
        }
    }

    @Override
    public Loader<String> onCreateLoader(final int id, final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Override
            protected void onStartLoading() {

                if (args == null) {
                    return;
                }

                forceLoad();
            }

            @Override
            public String loadInBackground() {
                if (GET_TRAILERS_LOADER == id) {
                    String getTrailerUrlString = args.getString(GET_TRAILERS_URL);

                    if (getTrailerUrlString == null) {
                        return null;
                    }

                    return getResponse(getTrailerUrlString);
                }

                if (GET_REVIEWS_LOADER == id) {
                    String getReviewUrlString = args.getString(GET_REVIEWS_URL);

                    if (getReviewUrlString == null) {
                        return null;
                    }

                    return getResponse(getReviewUrlString);
                }
                return null;
            }
        };
    }

    private String getResponse(String requestUrl) {
        try {
            URL getTrailerUrl = new URL(requestUrl);
            return NetworkUtils.getResponseFromHttpUrl(getTrailerUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String getResponseResults) {

        if (getResponseResults != null && !getResponseResults.equals("")) {
            try {
                JSONObject response = new JSONObject(getResponseResults);
                JSONArray results = response.getJSONArray(Constants.RESULT_KEY);
                if (GET_TRAILERS_LOADER == loader.getId()) {
                    mTrailerAdapter.clearItems();
                    for (int i = 0; i < results.length(); i++) {
                        mTrailerAdapter.addTrailerItem(new Trailer(results.getJSONObject(i)));
                    }
                    mTrailerAdapter.notifyDataSetChanged();
                }
                if (GET_REVIEWS_LOADER == loader.getId()) {
                    mReviewAdapter.clearItems();
                    for (int i = 0; i < results.length(); i++) {
                        mReviewAdapter.addReviewItem(new Review(results.getJSONObject(i)));
                    }
                    mReviewAdapter.notifyDataSetChanged();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onListItemClick(String videoKey) {
        Intent intent;
        if (isYoutubeInstalled()) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.YOUTUBE_VIDEO_URL_PREFIX + videoKey));
            startActivity(intent);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.BASE_VIDEO_URL_PREFIX + videoKey));
            startActivity(Intent.createChooser(intent, "Complete action using"));
        }
    }

    private boolean isYoutubeInstalled() {
        return getPackageManager().getLaunchIntentForPackage("com.google.android.youtube") != null;
    }
}
