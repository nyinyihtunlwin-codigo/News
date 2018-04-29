package com.nyinyihtunlwin.news.network;

public interface NewsDataAgent {

    void loadNews(String apiKey, int pageNo, String country);

    void searchNews(String apiKey, int pageNo, String query,String source);

    void loadSources(String apiKey);
}
