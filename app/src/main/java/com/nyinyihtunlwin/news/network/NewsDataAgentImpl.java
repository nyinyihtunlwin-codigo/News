package com.nyinyihtunlwin.news.network;

import com.google.gson.Gson;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.network.responses.NewsResponse;

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
                    NewsEvents.RestAPIEvent noDataEvent = new NewsEvents.RestAPIEvent("No news available.",1);
                    EventBus.getDefault().post(noDataEvent);
                }
            }
        });
    }
}
