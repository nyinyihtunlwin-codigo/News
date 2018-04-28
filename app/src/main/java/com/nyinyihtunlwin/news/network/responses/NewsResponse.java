package com.nyinyihtunlwin.news.network.responses;

import com.google.gson.annotations.SerializedName;
import com.nyinyihtunlwin.news.data.vos.NewsVO;

import java.util.List;

public class NewsResponse extends BaseResponse{

    @SerializedName("status")
    private String status;

    @SerializedName("totalResults")
    private int totalResults;

    @SerializedName("articles")
    private List<NewsVO> news;

    public String getStatus() {
        return status;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public List<NewsVO> getNews() {
        return news;
    }
}
