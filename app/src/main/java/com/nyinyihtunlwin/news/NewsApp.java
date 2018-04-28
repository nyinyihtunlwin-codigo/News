package com.nyinyihtunlwin.news;

import android.app.Application;

import com.nyinyihtunlwin.news.data.models.NewsModel;
import com.nyinyihtunlwin.news.utils.AppUtils;
import com.nyinyihtunlwin.news.utils.ConfigUtils;

public class NewsApp extends Application {

    public static final String LOG_TAG = "NEWS";

    @Override
    public void onCreate() {
        super.onCreate();
        ConfigUtils.initConfigUtils(getApplicationContext());
        AppUtils.initAppUtils(getApplicationContext());
    }
}
