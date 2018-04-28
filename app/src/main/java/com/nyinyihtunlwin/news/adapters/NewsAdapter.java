package com.nyinyihtunlwin.news.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.ViewGroup;

import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.viewholders.NewsViewHolder;

public class NewsAdapter extends BaseRecyclerAdapter<NewsViewHolder, NewsVO> {

    public NewsAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }
}
