package com.nyinyihtunlwin.news.acitivities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.nyinyihtunlwin.news.R;
import com.nyinyihtunlwin.news.adapters.NewsAdapter;
import com.nyinyihtunlwin.news.components.EmptyViewPod;
import com.nyinyihtunlwin.news.components.SmartRecyclerView;
import com.nyinyihtunlwin.news.components.SmartScrollListener;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.mvp.presenters.NewsPresenter;
import com.nyinyihtunlwin.news.mvp.views.NewsView;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NewsView {

    @BindView(R.id.rv_news)
    SmartRecyclerView rvNews;

    @BindView(R.id.vp_empty_news)
    EmptyViewPod emptyViewPod;

    @BindView(R.id.swipe_refresh_news)
    SwipeRefreshLayout swipeRefreshLayout;

    private NewsPresenter mPresenter;
    private NewsAdapter mAdapter;
    private SmartScrollListener mScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this, this);

        mPresenter = new NewsPresenter();
        mPresenter.onCreate(this);

        rvNews.setEmptyView(emptyViewPod);
        mAdapter = new NewsAdapter(this);
        rvNews.setAdapter(mAdapter);

        mScrollListener = new SmartScrollListener(new SmartScrollListener.OnSmartScrollListener() {
            @Override
            public void onListEndReached() {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        mPresenter.onStartLoadingNews();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPresenter.onStop();
    }

    @Override
    public void displayNews(List<NewsVO> news) {

    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showPrompt(String message) {
        final Snackbar snackbar = Snackbar.make(rvNews, message, Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        }).show();
    }
}
