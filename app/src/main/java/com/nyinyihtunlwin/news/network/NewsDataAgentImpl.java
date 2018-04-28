package com.nyinyihtunlwin.news.network;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NewsDataAgentImpl {

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
                .baseUrl("https://newsapi.org/v2")
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

}
