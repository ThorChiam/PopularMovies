package com.shentuo.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShentuoZhan on 13/3/17.
 */

public class Poster {
    private String poster_path;
    private boolean adult;
    private String overview;
    private String release_date;
    private int id;
    private String original_title;
    private String original_language;
    private String title;
    private String backdrop_path;
    private double popularity;
    private int vote_count;
    private boolean video;
    private double vote_average;

    public Poster(JSONObject object) {
        try {
            this.poster_path = object.getString("poster_path");
            this.adult = object.getBoolean("adult");
            this.overview = object.getString("overview");
            this.release_date = object.getString("release_date");
            this.id = object.getInt("id");
            this.original_title = object.getString("original_title");
            this.original_language = object.getString("original_language");
            this.title = object.getString("title");
            this.backdrop_path = object.getString("backdrop_path");
            this.popularity = object.getDouble("popularity");
            this.vote_count = object.getInt("vote_count");
            this.video = object.getBoolean("video");
            this.vote_average = object.getDouble("vote_average");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public int getVote_count() {
        return vote_count;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("poster_path", poster_path);
            jsonObject.put("adult", adult);
            jsonObject.put("overview", overview);
            jsonObject.put("release_date", release_date);
            jsonObject.put("id", id);
            jsonObject.put("original_title", original_title);
            jsonObject.put("original_language", original_language);
            jsonObject.put("title", title);
            jsonObject.put("backdrop_path", backdrop_path);
            jsonObject.put("popularity", popularity);
            jsonObject.put("vote_count", vote_count);
            jsonObject.put("video", video);
            jsonObject.put("vote_average", vote_average);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
