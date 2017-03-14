package com.shentuo.popularmovies.ui.utilities;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.URL;

/**
 * Created by ShentuoZhan on 14/3/17.
 */

public class GetMoviePostersTask extends AsyncTask<URL, Void, String> {
    private AsyncTaskCompleteListener<String> listener;

    public GetMoviePostersTask(AsyncTaskCompleteListener<String> listener) {
        this.listener = listener;
    }

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
        super.onPostExecute(getMoviesResults);
        if (listener != null) {
            listener.onTaskComplete(getMoviesResults);
        }
    }
}
