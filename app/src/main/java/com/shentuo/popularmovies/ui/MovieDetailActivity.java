package com.shentuo.popularmovies.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.shentuo.popularmovies.global.Constants;
import com.shentuo.popularmovies.model.Poster;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShentuoZhan on 13/3/17.
 */

public class MovieDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent.hasExtra(Constants.EXTRA_KEY)) {
            String jsonString = intent.getStringExtra(Constants.EXTRA_KEY);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                Poster poster = new Poster(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
