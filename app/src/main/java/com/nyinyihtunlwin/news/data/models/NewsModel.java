package com.nyinyihtunlwin.news.data.models;

import com.nyinyihtunlwin.news.network.NewsDataAgentImpl;
import com.nyinyihtunlwin.news.utils.AppConstants;

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

    public void startLoadingNews() {
        NewsDataAgentImpl.getObjectInstance().loadNews(AppConstants.API_KEY, 1, "us");
    }


}
