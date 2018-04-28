package com.nyinyihtunlwin.news.data.models;

public class NewsModel {

    private static NewsModel objectInstance;

    public static NewsModel getInstance() {
        if (objectInstance == null) {
            objectInstance = new NewsModel();
        }
        return objectInstance;
    }

    private NewsModel() {

    }
}
