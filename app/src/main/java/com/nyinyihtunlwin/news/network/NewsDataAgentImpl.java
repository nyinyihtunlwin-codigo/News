package com.nyinyihtunlwin.news.network;

import com.google.gson.Gson;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.events.SearchEvents;
import com.nyinyihtunlwin.news.events.SourcesEvents;
import com.nyinyihtunlwin.news.network.responses.NewsResponse;
import com.nyinyihtunlwin.news.network.responses.SourceResponse;

import org.greenrobot.eventbus.EventBus;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsDataAgentImpl implements NewsDataAgent {

    private static NewsDataAgentImpl objectInstance;

    private NewsAPI newsAPI;

    private NewsDataAgentImpl() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        // time 60 sec is optimal.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .client(okHttpClient)
                .build();

        newsAPI = retrofit.create(NewsAPI.class);
    }

    public static NewsDataAgentImpl getObjectInstance() {
        if (objectInstance == null) {
            objectInstance = new NewsDataAgentImpl();
        }
        return objectInstance;
    }

    @Override
    public void loadNews(String apiKey, int pageNo, String country) {
        Call<NewsResponse> loadNewsCall = newsAPI.loadNews(apiKey, pageNo, "US");
        loadNewsCall.enqueue(new NewsCallback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                NewsResponse newsResponse = response.body();
                if (newsResponse != null && newsResponse.getNews().size() > 0) {
                    NewsEvents.NewsLoadedEvent event = new NewsEvents.NewsLoadedEvent(newsResponse.getNews());
                    EventBus.getDefault().post(event);
                } else {
                    NewsEvents.RestAPIEvent noDataEvent = new NewsEvents.RestAPIEvent("No news available.", 1);
                    EventBus.getDefault().post(noDataEvent);
                }
            }
        });
    }

    @Override
    public void searchNews(String apiKey, int pageNo, String query) {
        Call<NewsResponse> loadNewsCall = newsAPI.searchNews(apiKey, pageNo, query);
        loadNewsCall.enqueue(new NewsCallback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                NewsResponse newsResponse = response.body();
                if (newsResponse != null && newsResponse.getNews().size() > 0) {
                    SearchEvents.NewsLoadedEvent event = new SearchEvents.NewsLoadedEvent(newsResponse.getNews());
                    EventBus.getDefault().post(event);
                } else {
                    SearchEvents.RestAPIEvent noDataEvent = new SearchEvents.RestAPIEvent("No news available.");
                    EventBus.getDefault().post(noDataEvent);
                }
            }
        });
    }

    @Override
    public void loadSources(String apiKey) {
        Call<SourceResponse> loadNewsCall = newsAPI.loadSources(apiKey, "us", "en");
        loadNewsCall.enqueue(new NewsCallback<SourceResponse>() {
            @Override
            public void onResponse(Call<SourceResponse> call, Response<SourceResponse> response) {
                SourceResponse newsResponse = response.body();
                if (newsResponse != null && newsResponse.getSources().size() > 0) {
                    SourcesEvents.SourceLoadedEvent event = new SourcesEvents.SourceLoadedEvent(newsResponse.getSources());
                    EventBus.getDefault().post(event);
                } else {
                    SourcesEvents.RestAPIEvent noDataEvent = new SourcesEvents.RestAPIEvent("Can't load sources.");
                    EventBus.getDefault().post(noDataEvent);
                }
            }
        });
    }
}
