package com.nyinyihtunlwin.news;

import android.app.Application;

import com.nyinyihtunlwin.news.utils.ConfigUtils;

public class NewsApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigUtils.initConfigUtils(getApplicationContext());
    }
}
