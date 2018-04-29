package com.nyinyihtunlwin.news.mvp.views;

import com.nyinyihtunlwin.news.data.vos.NewsVO;

import java.util.List;


public interface SearchView {

    void displaySearchResults(List<NewsVO> resultList);

    void showLoding();

    void navigateToDetails(String url);

    void showErrorMsg(String message);
}
