package com.shentuo.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShentuoZhan on 24/4/17.
 */

public class Review {
    private String id;
    private String author;
    private String content;
    private String url;

    public Review(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.author = object.getString("author");
            this.content = object.getString("content");
            this.url = object.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("author", author);
            jsonObject.put("content", content);
            jsonObject.put("url", url);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
