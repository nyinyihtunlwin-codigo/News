package com.nyinyihtunlwin.news.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.nyinyihtunlwin.news.R;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.delegates.NewsItemDelegate;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsViewHolder extends BaseViewHolder<NewsVO> {

    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_description)
    TextView tvDescription;

    @BindView(R.id.iv_news)
    ImageView ivNews;

    @BindView(R.id.lb_author)
    TextView lbAuthor;

    @BindView(R.id.tv_author)
    TextView tvAuthor;

    @BindView(R.id.tv_source)
    TextView tvSource;

    @BindView(R.id.lb_date)
    TextView lbDate;

    @BindView(R.id.tv_date)
    TextView tvDate;

    private NewsItemDelegate mNewsItemDelegate;
    private NewsVO mData;

    public NewsViewHolder(View itemView, NewsItemDelegate newsItemDelegate) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.mNewsItemDelegate = newsItemDelegate;
    }

    @Override
    public void setData(NewsVO mData) {
        if (mData != null) {
            this.mData = mData;
            if (mData.getTitle() != null) {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(mData.getTitle());
            }
            if (mData.getDescription() != null) {
                tvDescription.setVisibility(View.VISIBLE);
                tvDescription.setText(mData.getDescription());
            }
            if (mData.getUrlToImage() != null) {
                ivNews.setVisibility(View.VISIBLE);
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.placeholder)
                        .centerCrop();
                Glide.with(itemView.getRootView().getContext()).load(mData.getUrlToImage())
                        .apply(requestOptions)
                        .into(ivNews);

            }
            if (mData.getAuthor() != null) {
                lbAuthor.setVisibility(View.VISIBLE);
                tvAuthor.setVisibility(View.VISIBLE);
                tvAuthor.setText(mData.getAuthor());
            }
            if (mData.getSource() != null && mData.getSource().getName() != null) {
                tvSource.setVisibility(View.VISIBLE);
                tvSource.setText(mData.getSource().getName());
            }
            if (mData.getPublishedAt() != null) {
                lbDate.setVisibility(View.VISIBLE);
                tvDate.setVisibility(View.VISIBLE);
                tvDate.setText(mData.getPublishedAt());
            }
        }
    }

    @Override
    public void onClick(View v) {
        mNewsItemDelegate.onTapNews(mData);
    }
}
