package com.nyinyihtunlwin.news.network;

import com.nyinyihtunlwin.news.data.vos.SourceVO;
import com.nyinyihtunlwin.news.network.responses.NewsResponse;
import com.nyinyihtunlwin.news.network.responses.SourceResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {

    @GET("v2/top-headlines")
    Call<NewsResponse> loadNews(@Query("apiKey") String apiKey, @Query("page") Integer page, @Query("country") String country);

    @GET("v2/everything")
    Call<NewsResponse> searchNews(@Query("apiKey") String apiKey, @Query("page") Integer page, @Query("q") String query, @Query("sources") String sources);

    @GET("v2/sources")
    Call<SourceResponse> loadSources(@Query("apiKey") String apiKey, @Query("language") String language, @Query("country") String country);

}
