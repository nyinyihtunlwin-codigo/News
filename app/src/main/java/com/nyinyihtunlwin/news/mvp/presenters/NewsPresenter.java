package com.nyinyihtunlwin.news.mvp.presenters;

import android.content.Context;
import android.database.Cursor;

import com.nyinyihtunlwin.news.data.models.NewsModel;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.mvp.views.NewsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class NewsPresenter extends BasePresenter<NewsView> {

    private Context mContext;

    public NewsPresenter(Context context) {
        this.mContext = context;
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
    }

    public void onStartLoadingNews() {
        mView.showLoading();
        NewsModel.getInstance().startLoadingNews();
    }

    @Subscribe
    public void onNewsLoaded(NewsEvents.NewsLoadedEvent event) {
        List<NewsVO> news = event.getNews();
        NewsModel.getInstance().saveToDb(mContext, news);
    }

    @Subscribe
    public void onRestAPIErrorLoaded(NewsEvents.RestAPIEvent event) {
        mView.showPrompt(event);
    }

    public void onDataLoaded(Context context, Cursor data) {
        if (data != null && data.moveToFirst()) {
            List<NewsVO> newsList = new ArrayList<>();
            do {
                NewsVO newsVO = NewsVO.parseFromCursor(context, data);
                newsList.add(newsVO);
            } while (data.moveToNext());
            mView.displayNews(newsList);
        } else {
            mView.showLoading();
            NewsModel.getInstance().startLoadingNews();
        }
    }

    public void tapOnForceRefresh() {
        NewsModel.getInstance().onForceRefresh();
    }

    public void onListEndReached() {
        NewsModel.getInstance().loadMoreNews();
    }
}
