package com.nyinyihtunlwin.news.mvp.views;

import com.nyinyihtunlwin.news.data.vos.NewsVO;

import java.util.List;

public interface NewsView {
    void displayNews(List<NewsVO> news);

    void showLoading();

    void showPrompt(String message);
}
