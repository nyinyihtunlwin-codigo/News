package com.nyinyihtunlwin.news.utils;

import android.content.Context;
import android.content.SharedPreferences;


public class ConfigUtils {

    private static final String KEY_SEARCH_RESULT_PAGE_INDEX = "KEY_SEARCH_RESULT_PAGE_INDEX";
    private static final String KEY_NEWS_PAGE_INDEX = "KEY_NEWS_PAGE_INDEX";

    private SharedPreferences mSharedPreferences;

    private static ConfigUtils sObjInstance;


    private ConfigUtils(Context context) {
        mSharedPreferences = context.getSharedPreferences("ConfigUtils", Context.MODE_PRIVATE);
    }

    public static void initConfigUtils(Context context) {
        sObjInstance = new ConfigUtils(context);
    }

    public static ConfigUtils getObjInstance() {
        return sObjInstance;
    }


    public void saveNewsPageIndex(int pageIndex) {
        mSharedPreferences.edit().putInt(KEY_NEWS_PAGE_INDEX, pageIndex).apply();
    }

    public int loadNewsPageIndex() {
        return mSharedPreferences.getInt(KEY_NEWS_PAGE_INDEX, 1);
    }

    public void saveSearchResultPageIndex(int pageIndex) {
        mSharedPreferences.edit().putInt(KEY_SEARCH_RESULT_PAGE_INDEX, pageIndex).apply();
    }

    public int loadSearchResultPageIndex() {
        return mSharedPreferences.getInt(KEY_SEARCH_RESULT_PAGE_INDEX, 1);
    }

}
