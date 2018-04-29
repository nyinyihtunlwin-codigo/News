package com.nyinyihtunlwin.news.acitivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nyinyihtunlwin.news.R;
import com.nyinyihtunlwin.news.adapters.NewsAdapter;
import com.nyinyihtunlwin.news.components.EmptyViewPod;
import com.nyinyihtunlwin.news.components.SmartRecyclerView;
import com.nyinyihtunlwin.news.components.SmartScrollListener;
import com.nyinyihtunlwin.news.data.models.NewsModel;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.delegates.NewsItemDelegate;
import com.nyinyihtunlwin.news.events.NewsEvents;
import com.nyinyihtunlwin.news.mvp.presenters.NewsPresenter;
import com.nyinyihtunlwin.news.mvp.views.NewsView;
import com.nyinyihtunlwin.news.persistence.NewsContract;
import com.nyinyihtunlwin.news.utils.AppConstants;

import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements NewsView, LoaderManager.LoaderCallbacks<Cursor>, NewsItemDelegate {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

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

        mPresenter = new NewsPresenter(getApplicationContext());
        mPresenter.onCreate(this);

        rvNews.setEmptyView(emptyViewPod);
        mAdapter = new NewsAdapter(this, this);
        rvNews.setAdapter(mAdapter);
        rvNews.setLayoutManager(new LinearLayoutManager(this));

        mScrollListener = new SmartScrollListener(new SmartScrollListener.OnSmartScrollListener() {
            @Override
            public void onListEndReached() {
                showLoadMore();
                mPresenter.onListEndReached();
            }
        });

        rvNews.addOnScrollListener(mScrollListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SearchActivity.newIntent(getApplicationContext());
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.tapOnForceRefresh();
            }
        });

        getSupportLoaderManager().initLoader(AppConstants.NEWS_LOADER_ID, null, MainActivity.this);
    }

    private void showLoadMore() {
        Snackbar snackbar = Snackbar.make(rvNews, "loading news...", Snackbar.LENGTH_LONG);
        View view = snackbar.getView();
        TextView textView = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(getResources().getColor(R.color.accent));
        snackbar.show();
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
        mAdapter.setNewData(news);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showLoading() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showPrompt(NewsEvents.RestAPIEvent event) {
        swipeRefreshLayout.setRefreshing(false);
        switch (event.getStatus()) {
            case 1: // Connection available...
                final Snackbar snackbar = Snackbar.make(rvNews, event.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                }).show();
                break;
            case 2: // On start loading and Refresh
                final Snackbar snackbarTry = Snackbar.make(rvNews, event.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbarTry.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarTry.dismiss();
                        mPresenter.onStartLoadingNews();
                    }
                }).show();
                break;
            case 3: // On load more
                final Snackbar snackbarTryLoadMore = Snackbar.make(rvNews, event.getMessage(), Snackbar.LENGTH_INDEFINITE);
                snackbarTryLoadMore.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbarTryLoadMore.dismiss();
                        mPresenter.onListEndReached();
                    }
                }).show();
                break;
        }

    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getApplicationContext(),
                NewsContract.NewsEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPresenter.onDataLoaded(getApplicationContext(), data);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }

    @Override
    public void onTapNews(NewsVO news) {
        Intent intent = DetailsActivity.newIntent(getApplicationContext(), news.getUrl());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

}
