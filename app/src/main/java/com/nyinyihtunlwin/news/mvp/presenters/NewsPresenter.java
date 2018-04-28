package com.nyinyihtunlwin.news.mvp.presenters;

import com.nyinyihtunlwin.news.data.models.NewsModel;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.mvp.views.NewsView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

public class NewsPresenter extends BasePresenter<NewsView> {


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
    }

    @Subscribe
    public void onRestAPIErrorLoaded(NewsEvents.RestAPIEvent event) {
        mView.showPrompt(event.getMessage());
    }
}
