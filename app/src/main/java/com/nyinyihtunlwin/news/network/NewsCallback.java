package com.nyinyihtunlwin.news.network;

import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.network.responses.BaseResponse;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;

public abstract class NewsCallback<T extends BaseResponse> implements Callback<T> {
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        NewsEvents.RestAPIEvent event = new NewsEvents.RestAPIEvent("Can't load data.", 1);
        EventBus.getDefault().post(event);
    }
}
