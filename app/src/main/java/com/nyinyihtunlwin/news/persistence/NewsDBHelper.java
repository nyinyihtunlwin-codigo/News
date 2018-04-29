package com.nyinyihtunlwin.news.persistence;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Dell on 12/16/2017.
 */

public class NewsDBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "mmnews.db";

    private static final String SQL_CREATE_NEWS = "CREATE TABLE " + NewsContract.NewsEntry.TABLE_NAME + " (" +
            NewsContract.NewsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NewsContract.NewsEntry.COLUMN_AUTHOR + " TEXT, " +
            NewsContract.NewsEntry.COLUMN_TITLE + " TEXT, " +
            NewsContract.NewsEntry.COLUMN_DESCRIPTION + " TEXT, " +
            NewsContract.NewsEntry.COLUMN_URL + " TEXT, " +
            NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE + " TEXT, " +
            NewsContract.NewsEntry.COLUMN_PUBLISHED_AT + " TEXT, " +
            NewsContract.NewsEntry.COLUMN_SOURCE_ID + " TEXT, " +
            " UNIQUE (" + NewsContract.NewsEntry.COLUMN_URL + ") ON CONFLICT REPLACE" +
            ");";

    private static final String SQL_CREATE_SOURCE = "CREATE TABLE " + NewsContract.SourceEntry.TABLE_NAME + " (" +
            NewsContract.SourceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            NewsContract.SourceEntry.COLUMN_SOURCE_ID + " VARCHAR(256), " +
            NewsContract.SourceEntry.COLUMN_NAME + " TEXT, " +
            " UNIQUE (" + NewsContract.SourceEntry.COLUMN_SOURCE_ID + ") ON CONFLICT IGNORE" +
            ");";

    public NewsDBHelper(Context context) {
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
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.NewsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NewsContract.SourceEntry.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}
