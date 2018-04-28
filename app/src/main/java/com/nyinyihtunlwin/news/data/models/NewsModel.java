package com.nyinyihtunlwin.news.data.models;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import com.nyinyihtunlwin.news.NewsApp;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.data.vos.SourceVO;
import com.nyinyihtunlwin.news.network.NewsDataAgentImpl;
import com.nyinyihtunlwin.news.persistence.MMNewsContract;
import com.nyinyihtunlwin.news.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

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


    public void saveToDb(Context context, List<NewsVO> news) {
        //TODO Logic to save the data in Persistence Layer
        ContentValues[] newsCVs = new ContentValues[news.size()];
        List<ContentValues> sourceCVList = new ArrayList<>();

        for (int index = 0; index < newsCVs.length; index++) {
            NewsVO newsVO = news.get(index);
            // newsCVs[index] = newsVO.parseToContentValues();

            SourceVO source = newsVO.getSource();
            if (source != null) {
                //  sourceCVList.add(source.parseToContentValues());
            }

        }

        int insertedSources = context.getContentResolver().bulkInsert(MMNewsContract.SourceEntry.CONTENT_URI,
                sourceCVList.toArray(new ContentValues[0]));
        Log.d(NewsApp.LOG_TAG, "inserted Sources :" + insertedSources);


        int insertedRowCount = context.getContentResolver().bulkInsert(MMNewsContract.NewsEntry.CONTENT_URI, newsCVs);
        Log.d(NewsApp.LOG_TAG, "Inserted row : " + insertedRowCount);
    }
}
