package com.shentuo.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.shentuo.popularmovies.R;
import com.shentuo.popularmovies.global.Constants;
import com.shentuo.popularmovies.model.Poster;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShentuoZhan on 13/3/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    private Poster poster;

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
        TextView tvTitle = (TextView) findViewById(R.id.movie_title);
        ImageView ivPoster = (ImageView) findViewById(R.id.movie_thumbnail);
        TextView tvOverview = (TextView) findViewById(R.id.movie_overview);
        TextView tvRating = (TextView) findViewById(R.id.user_rating);
        TextView tvReleaseDate = (TextView) findViewById(R.id.release_date);

        if (poster != null) {
            tvTitle.setText(poster.getOriginal_title());
            String imageURL = Constants.BASE_IMAGE_URL + Constants.THUMBNAIL_SIZE + "/" + poster.getPoster_path();
            Picasso.with(this).load(imageURL).into(ivPoster);
            tvOverview.setText(poster.getOverview());
            String userRate = getResources().getString(R.string.user_rate) + poster.getVote_average();
            tvRating.setText(userRate);
            String releaseDate = getResources().getString(R.string.release_date) + poster.getRelease_date();
            tvReleaseDate.setText(releaseDate);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
