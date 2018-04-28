package com.nyinyihtunlwin.news.network;

import com.nyinyihtunlwin.news.network.responses.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsAPI {

    @GET("v2/top-headlines")
    Call<NewsResponse> loadNews(@Query("apiKey") String apiKey, @Query("page") Integer page, @Query("country") String country);

}
