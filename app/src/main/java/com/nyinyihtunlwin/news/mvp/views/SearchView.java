package com.nyinyihtunlwin.news.mvp.views;

import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.data.vos.SourceVO;

import java.util.List;


public interface SearchView {

    void displaySearchResults(List<NewsVO> resultList);

    void displaySources(List<SourceVO> sourceList);

    void showLoding();

    void navigateToDetails(String url);

    void showErrorMsg(String message);
}
