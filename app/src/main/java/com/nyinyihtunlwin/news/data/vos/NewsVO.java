package com.nyinyihtunlwin.news.data.vos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.google.gson.annotations.SerializedName;
import com.nyinyihtunlwin.news.persistence.NewsContract;

public class NewsVO {

    @SerializedName("source")
    private SourceVO source;

    @SerializedName("author")
    private String author;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("url")
    private String url;

    @SerializedName("urlToImage")
    private String urlToImage;

    @SerializedName("publishedAt")
    private String publishedAt;

    public SourceVO getSource() {
        return source;
    }

    public void setSource(SourceVO source) {
        this.source = source;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public ContentValues parseToContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(NewsContract.NewsEntry.COLUMN_AUTHOR, author);
        contentValues.put(NewsContract.NewsEntry.COLUMN_TITLE, title);
        contentValues.put(NewsContract.NewsEntry.COLUMN_DESCRIPTION, description);
        contentValues.put(NewsContract.NewsEntry.COLUMN_URL, url);
        contentValues.put(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE, urlToImage);
        contentValues.put(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT, publishedAt);
        contentValues.put(NewsContract.NewsEntry.COLUMN_SOURCE_ID, source.getId());

        return contentValues;
    }

    public static NewsVO parseFromCursor(Context context, Cursor cursor) {
        NewsVO news = new NewsVO();
        news.author = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_AUTHOR));
        news.title = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_TITLE));
        news.description = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_DESCRIPTION));
        news.url = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL));
        news.urlToImage = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_URL_TO_IMAGE));
        news.publishedAt = cursor.getString(cursor.getColumnIndex(NewsContract.NewsEntry.COLUMN_PUBLISHED_AT));

        news.source = SourceVO.parseFromCursor(cursor);
        return news;
    }
}
