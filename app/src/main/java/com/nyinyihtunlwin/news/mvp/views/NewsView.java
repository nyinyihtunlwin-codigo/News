package com.nyinyihtunlwin.news.mvp.views;

import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.events.NewsEvents;

import java.util.List;

public interface NewsView {
    void displayNews(List<NewsVO> news);

    void showLoading();

    void showPrompt(NewsEvents.RestAPIEvent event);
}
