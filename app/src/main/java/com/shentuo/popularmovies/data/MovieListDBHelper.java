package com.shentuo.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shentuo.popularmovies.data.MovieListContract.MovieListEntry;

/**
 * Created by ShentuoZhan on 24/4/17.
 */

public class MovieListDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "moviesDb.db";

    private static final int DATABASE_VERSION = 1;

    public MovieListDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_MOVIELIST_TABLE = "CREATE TABLE " + MovieListEntry.TABLE_NAME + " (" +
                MovieListEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                MovieListEntry.COLUMN_TITLE + " TEXT, " +
                MovieListEntry.COLUMN_POSTER_PATH + " TEXT, " +
                MovieListEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieListEntry.COLUMN_VOTE_AVERAGE + " REAL, " +
                MovieListEntry.COLUMN_RELEASE_DATE + " TEXT " +
                "); ";

        db.execSQL(SQL_CREATE_MOVIELIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieListEntry.TABLE_NAME);
        onCreate(db);
    }
}
