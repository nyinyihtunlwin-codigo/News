package com.nyinyihtunlwin.news.data.vos;

import com.google.gson.annotations.SerializedName;

public class SourceVO {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
