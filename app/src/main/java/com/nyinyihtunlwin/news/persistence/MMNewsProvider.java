package com.nyinyihtunlwin.news.persistence;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by Dell on 12/16/2017.
 */

public class MMNewsProvider extends ContentProvider {

    public static final int SOURCE = 200;
    public static final int NEWS = 300;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final SQLiteQueryBuilder sNewsWithSource_IJ;

    static {
        sNewsWithSource_IJ = new SQLiteQueryBuilder();
        sNewsWithSource_IJ.setTables(
                MMNewsContract.NewsEntry.TABLE_NAME + " INNER JOIN " +
                        MMNewsContract.SourceEntry.TABLE_NAME +
                        " ON " +
                        MMNewsContract.NewsEntry.TABLE_NAME + "." + MMNewsContract.NewsEntry.COLUMN_SOURCE_ID + " = " +
                        MMNewsContract.SourceEntry.TABLE_NAME + "." + MMNewsContract.SourceEntry.COLUMN_SOURCE_ID
        );
    }

    private MMNewsDBHelper mDBHelper;

    private static UriMatcher buildUriMatcher() {

        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MMNewsContract.CONTENT_AUTHORITY, MMNewsContract.PATH_SOURCE, SOURCE);
        uriMatcher.addURI(MMNewsContract.CONTENT_AUTHORITY, MMNewsContract.PATH_NEWS, NEWS);


        return uriMatcher;
    }

    private String getTableName(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SOURCE:
                return MMNewsContract.SourceEntry.TABLE_NAME;
            case NEWS:
                return MMNewsContract.NewsEntry.TABLE_NAME;
        }
        return null;
    }

    private Uri getContentUri(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case SOURCE:
                return MMNewsContract.SourceEntry.CONTENT_URI;
            case NEWS:
                return MMNewsContract.NewsEntry.CONTENT_URI;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        mDBHelper = new MMNewsDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor queryCursor;
        switch (sUriMatcher.match(uri)) {
            case NEWS:
                queryCursor = sNewsWithSource_IJ.query(mDBHelper.getReadableDatabase(),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                queryCursor = mDBHelper.getReadableDatabase().query(getTableName(uri),
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
        }
        if (getContext() != null) {
            queryCursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return queryCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int matchUri = sUriMatcher.match(uri);
        switch (matchUri) {
            case SOURCE:
                return MMNewsContract.SourceEntry.DIR_TYPE;
            case NEWS:
                return MMNewsContract.NewsEntry.DIR_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String tableName = getTableName(uri);
        long _id = db.insert(tableName, null, contentValues);
        if (_id > 0) {
            Uri tableContentUri = getContentUri(uri);
            Uri insertedUri = ContentUris.withAppendedId(tableContentUri, _id);
            if (getContext() != null) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return insertedUri;
        }
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        String tableName = getTableName(uri);
        int insertedCount = 0;

        try {
            db.beginTransaction();
            for (ContentValues cv : values) {
                long _id = db.insert(tableName, null, cv);
                if (_id > 0) {
                    insertedCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return insertedCount;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowDeleted;
        String tableName = getTableName(uri);
        rowDeleted = db.delete(tableName, selection, selectionArgs);
        Context context = getContext();
        if (context != null && rowDeleted > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mDBHelper.getWritableDatabase();
        int rowUpdated;
        String tableName = getTableName(uri);

        rowUpdated = db.update(tableName, contentValues, selection, selectionArgs);
        Context context = getContext();
        if (context != null && rowUpdated > 0) {
            context.getContentResolver().notifyChange(uri, null);
        }
        return rowUpdated;
    }
}
