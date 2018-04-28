package com.nyinyihtunlwin.news.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 12/16/2017.
 */

public class MMNewsDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mmnews.db";

    private static final String SQL_CREATE_NEWS = "CREATE TABLE " + MMNewsContract.NewsEntry.TABLE_NAME + " (" +
            MMNewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MMNewsContract.NewsEntry.COLUMN_AUTHOR + " TEXT, " +
            MMNewsContract.NewsEntry.COLUMN_TITLE + " TEXT, " +
            MMNewsContract.NewsEntry.COLUMN_DESCRIPTION + " TEXT, " +
            MMNewsContract.NewsEntry.COLUMN_URL + " TEXT, " +
            MMNewsContract.NewsEntry.COLUMN_URL_TO_IMAGE + " TEXT, " +
            MMNewsContract.NewsEntry.COLUMN_PUBLISHED_AT + " TEXT, " +
            MMNewsContract.NewsEntry.COLUMN_SOURCE_ID + " TEXT, " +
            " UNIQUE (" + MMNewsContract.NewsEntry.COLUMN_URL + ") ON CONFLICT REPLACE" +
            ");";

    private static final String SQL_CREATE_SOURCE = "CREATE TABLE " + MMNewsContract.SourceEntry.TABLE_NAME + " (" +
            MMNewsContract.SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MMNewsContract.SourceEntry.COLUMN_SOURCE_ID + " VARCHAR(256), " +
            MMNewsContract.SourceEntry.COLUMN_NAME + " TEXT, " +
            " UNIQUE (" + MMNewsContract.SourceEntry.COLUMN_SOURCE_ID + ") ON CONFLICT REPLACE" +
            ");";

    public MMNewsDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /**
         * NOTE : Start execute from leaf tables ( Don't start from root table)
         */
        sqLiteDatabase.execSQL(SQL_CREATE_SOURCE);
        sqLiteDatabase.execSQL(SQL_CREATE_NEWS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        /**
         * NOTE : Start drop table from root tables.
         */
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MMNewsContract.NewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MMNewsContract.SourceEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
