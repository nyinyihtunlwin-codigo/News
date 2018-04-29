package com.nyinyihtunlwin.news.network.responses;

import com.google.gson.annotations.SerializedName;
import com.nyinyihtunlwin.news.data.vos.SourceVO;

import java.util.List;

public class SourceResponse extends BaseResponse {

    @SerializedName("status")
    private String status;


    @SerializedName("sources")
    private List<SourceVO> sources;

    public String getStatus() {
        return status;
    }

    public List<SourceVO> getSources() {
        return sources;
    }
}
