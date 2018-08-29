package com.nyinyihtunlwin.news.data.models;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.nyinyihtunlwin.news.BuildConfig;
import com.nyinyihtunlwin.news.NewsApp;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.data.vos.SourceVO;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.network.NewsDataAgentImpl;
import com.nyinyihtunlwin.news.persistence.NewsContract;
import com.nyinyihtunlwin.news.utils.AppConstants;
import com.nyinyihtunlwin.news.utils.AppUtils;
import com.nyinyihtunlwin.news.utils.ConfigUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class NewsModel {


    private static NewsModel objectInstance;

    private String mQuery, mSource;

    public static NewsModel getInstance() {
        if (objectInstance == null) {
            objectInstance = new NewsModel();
        }
        return objectInstance;
    }

    private NewsModel() {
    }

    public void startLoadingNews() {
        ConfigUtils.getObjInstance().saveNewsPageIndex(1);
        loadNews(AppConstants.KEY_START_LOADING);
    }


    public void saveToDb(Context context, List<NewsVO> news) {
        //TODO Logic to save the data in Persistence Layer
        ContentValues[] newsCVs = new ContentValues[news.size()];
        List<ContentValues> sourceCVList = new ArrayList<>();

        for (int index = 0; index < newsCVs.length; index++) {
            NewsVO newsVO = news.get(index);
            newsCVs[index] = newsVO.parseToContentValues();

            SourceVO source = newsVO.getSource();
            if (source != null) {
                sourceCVList.add(source.parseToContentValues());
            }

        }

        int insertedSources = context.getContentResolver().bulkInsert(NewsContract.SourceEntry.CONTENT_URI,
                sourceCVList.toArray(new ContentValues[0]));
        Log.d(NewsApp.LOG_TAG, "inserted Sources :" + insertedSources);


        int insertedRowCount = context.getContentResolver().bulkInsert(NewsContract.NewsEntry.CONTENT_URI, newsCVs);
        Log.d(NewsApp.LOG_TAG, "Inserted row : " + insertedRowCount);
    }

    public void onForceRefresh() {
        ConfigUtils.getObjInstance().saveNewsPageIndex(1);
        loadNews(AppConstants.KEY_START_LOADING);
    }

    public void loadMoreNews() {
        ConfigUtils.getObjInstance().saveNewsPageIndex(ConfigUtils.getObjInstance().loadNewsPageIndex() + 1);
        loadNews(AppConstants.KEY_LOAD_MORE);
    }

    public void loadNews(int loadingStatus) {
        if (AppUtils.getObjInstance().hasConnection()) {
            NewsDataAgentImpl.getObjectInstance().loadNews(BuildConfig.APIKEY, ConfigUtils.getObjInstance().loadNewsPageIndex(), "us");
        } else {
            NewsEvents.RestAPIEvent event = new NewsEvents.RestAPIEvent("No internet connection.", loadingStatus);
            EventBus.getDefault().post(event);
        }
    }

    public void startSearching(String query, String source) {
        ConfigUtils.getObjInstance().saveSearchResultPageIndex(1);
        NewsDataAgentImpl.getObjectInstance().searchNews(BuildConfig.APIKEY, ConfigUtils.getObjInstance().loadSearchResultPageIndex(), query, source);
        mQuery = query;
        mSource = source;
    }

    public void loadMoreResults(String mQuery) {
        ConfigUtils.getObjInstance().saveSearchResultPageIndex(ConfigUtils.getObjInstance().loadSearchResultPageIndex() + 1);
        if (mQuery != null) {
            NewsDataAgentImpl.getObjectInstance().searchNews(BuildConfig.APIKEY, ConfigUtils.getObjInstance().loadSearchResultPageIndex(), mQuery, mSource);
        }
    }

    public void loadSources() {
        if (AppUtils.getObjInstance().hasConnection()) {
            NewsDataAgentImpl.getObjectInstance().loadSources(BuildConfig.APIKEY);
        } else {
            NewsEvents.RestAPIEvent event = new NewsEvents.RestAPIEvent("No internet connection.", AppConstants.KEY_START_LOADING);
            EventBus.getDefault().post(event);
        }
    }

    public void saveSourcesToDb(Context context, List<SourceVO> sources) {

        List<ContentValues> sourceCVList = new ArrayList<>();
        for (int index = 0; index < sources.size(); index++) {
            SourceVO sourceVO = sources.get(index);
            if (sourceVO != null) {
                sourceCVList.add(sourceVO.parseToContentValues());
            }
        }

        int insertedSources = context.getContentResolver().bulkInsert(NewsContract.SourceEntry.CONTENT_URI,
                sourceCVList.toArray(new ContentValues[0]));
        Log.d(NewsApp.LOG_TAG, "inserted Sources :" + insertedSources);

    }
}
