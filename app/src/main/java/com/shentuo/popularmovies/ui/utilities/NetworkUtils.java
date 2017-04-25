package com.shentuo.popularmovies.ui.utilities;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.shentuo.popularmovies.global.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by ShentuoZhan on 13/3/17.
 */

public final class NetworkUtils {
    private final static String BASE_URL = "https://api.themoviedb.org/3";
    private final static String PARAM_API_KEY = "api_key";
    private final static String TAG = "NetworkUtils";

    private NetworkUtils() {
        throw new AssertionError();
    }

    public static URL buildUrlForMostPopular() {
        String requestUrl = BASE_URL + "/movie/popular";
        Uri builtUri = Uri.parse(requestUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, Constants.REQUEST_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return url;
    }

    public static URL buildUrlForTopRated() {
        String requestUrl = BASE_URL + "/movie/top_rated";
        Uri builtUri = Uri.parse(requestUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, Constants.REQUEST_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return url;
    }

    public static URL buildUrlForTrailers(int movieId) {
        String requestUrl = BASE_URL + "/movie/" + movieId + "/videos";
        Uri builtUri = Uri.parse(requestUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, Constants.REQUEST_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return url;
    }

    public static URL buildUrlForReviews(int movieId) {
        String requestUrl = BASE_URL + "/movie/" + movieId + "/reviews";
        Uri builtUri = Uri.parse(requestUrl).buildUpon()
                .appendQueryParameter(PARAM_API_KEY, Constants.REQUEST_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static boolean isOnline(Activity activity) {
        ConnectivityManager cm =
                (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
