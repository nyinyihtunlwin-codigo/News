package com.nyinyihtunlwin.news.mvp.presenters;

import android.content.Context;

import com.nyinyihtunlwin.news.mvp.views.SearchView;
import com.nyinyihtunlwin.news.utils.ConfigUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;



public class SearchPresenter extends BasePresenter<SearchView> {

    private Context mContext;
    private String mQuery;

    public SearchPresenter(Context context) {
        this.mContext = context;
    }

    public void onTapSearch(String query) {
        mView.showLoding();
        mQuery = query;
        SearchResultModel.getInstance().startSearching(query);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onLoadedSearchResults(SearchEvents.SearchResultsDataLoadedEvent event) {
        ConfigUtils.getObjInstance().saveSearchResultPageIndex(event.getLoadedPageIndex());
        mView.displaySearchResults(event.getLoadedSearchResults());
    }

    @Subscribe
    public void onErrorInvokingAPILoaded(SearchEvents.ErrorInvokingAPIEvent event) {
        mView.showErrorMsg(event.getErrorMsg());
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }


    public void onTapResult(String movieId, String mediaType) {
        mView.navigateToDetails(movieId,mediaType);
    }

    public void onResultListEndReached() {
        SearchResultModel.getInstance().loadMoreResults(mQuery);
    }
}
