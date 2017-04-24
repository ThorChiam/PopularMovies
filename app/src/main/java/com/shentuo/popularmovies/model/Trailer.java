package com.shentuo.popularmovies.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ShentuoZhan on 24/4/17.
 */

public class Trailer {
    private String id;
    private String iso_639_1;
    private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public Trailer(JSONObject object) {
        try {
            this.id = object.getString("id");
            this.iso_639_1 = object.getString("iso_639_1");
            this.iso_3166_1 = object.getString("iso_3166_1");
            this.key = object.getString("key");
            this.name = object.getString("name");
            this.site = object.getString("site");
            this.size = object.getInt("size");
            this.type = object.getString("type");
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

    public String getIso_639_1() {
        return iso_639_1;
    }

    public void setIso_639_1(String iso_639_1) {
        this.iso_639_1 = iso_639_1;
    }

    public String getIso_3166_1() {
        return iso_3166_1;
    }

    public void setIso_3166_1(String iso_3166_1) {
        this.iso_3166_1 = iso_3166_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", id);
            jsonObject.put("iso_639_1", iso_639_1);
            jsonObject.put("iso_3166_1", iso_3166_1);
            jsonObject.put("key", key);
            jsonObject.put("name", name);
            jsonObject.put("site", site);
            jsonObject.put("size", size);
            jsonObject.put("type", type);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }
}
