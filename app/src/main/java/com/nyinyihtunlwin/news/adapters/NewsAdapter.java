package com.nyinyihtunlwin.news.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.nyinyihtunlwin.news.R;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.delegates.NewsItemDelegate;
import com.nyinyihtunlwin.news.viewholders.NewsViewHolder;

public class NewsAdapter extends BaseRecyclerAdapter<NewsViewHolder, NewsVO> {

    private NewsItemDelegate mNewsItemDelegate;

    public NewsAdapter(Context context, NewsItemDelegate newsItemDelegate) {
        super(context);
        this.mNewsItemDelegate=newsItemDelegate;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.view_item_news, parent, false);
        return new NewsViewHolder(view,mNewsItemDelegate);
    }
}
