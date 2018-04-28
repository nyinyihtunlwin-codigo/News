package com.nyinyihtunlwin.news.data.vos;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.nyinyihtunlwin.news.persistence.NewsContract;

public class SourceVO {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ContentValues parseToContentValues() {

        ContentValues contentValues = new ContentValues();

        contentValues.put(NewsContract.SourceEntry.COLUMN_SOURCE_ID, id);
        contentValues.put(NewsContract.SourceEntry.COLUMN_NAME, name);

        return contentValues;
    }

    public static SourceVO parseFromCursor(Cursor cursor) {

        SourceVO source = new SourceVO();

        source.id = cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry.COLUMN_SOURCE_ID));
        source.name = cursor.getString(cursor.getColumnIndex(NewsContract.SourceEntry.COLUMN_NAME));

        return source;
    }
}
