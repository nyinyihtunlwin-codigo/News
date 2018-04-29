package com.nyinyihtunlwin.news.mvp.presenters;

import android.content.Context;
import android.database.Cursor;

import com.nyinyihtunlwin.news.data.models.NewsModel;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.data.vos.SourceVO;
import com.nyinyihtunlwin.news.events.SearchEvents;
import com.nyinyihtunlwin.news.mvp.views.SearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


public class SearchPresenter extends BasePresenter<SearchView> {

    private Context mContext;
    private String mQuery;

    public SearchPresenter(Context context) {
        this.mContext = context;
    }

    public void onTapSearch(String query,String source) {
        mView.showLoding();
        mQuery = query;
        NewsModel.getInstance().startSearching(query,source);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onLoadedSearchResults(SearchEvents.NewsLoadedEvent event) {
        mView.displaySearchResults(event.getNews());
    }

    @Subscribe
    public void onErrorInvokingAPILoaded(SearchEvents.RestAPIEvent event) {
        mView.showErrorMsg(event.getMessage());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }


    public void onTapResult(String url) {
        mView.navigateToDetails(url);
    }

    public void onResultListEndReached() {
        NewsModel.getInstance().loadMoreResults(mQuery);
    }

    public void onDataLoaded(Context context, Cursor data) {
        if (data != null && data.moveToFirst()) {
            List<SourceVO> sourceList = new ArrayList<>();
            do {
                SourceVO sourceVO = SourceVO.parseFromCursor(data);
                sourceList.add(sourceVO);
            } while (data.moveToNext());
            mView.displaySources(sourceList);
        }
    }

}
