package com.nyinyihtunlwin.news.acitivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.nyinyihtunlwin.news.R;
import com.nyinyihtunlwin.news.adapters.NewsAdapter;
import com.nyinyihtunlwin.news.components.SmartRecyclerView;
import com.nyinyihtunlwin.news.components.SmartScrollListener;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.delegates.NewsItemDelegate;
import com.nyinyihtunlwin.news.mvp.presenters.SearchPresenter;
import com.nyinyihtunlwin.news.mvp.views.SearchView;
import com.nyinyihtunlwin.news.utils.AppUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements NewsItemDelegate, SearchView {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, SearchActivity.class);
        return intent;
    }

    @BindView(R.id.et_search)
    EditText etSearch;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @BindView(R.id.rv_result)
    SmartRecyclerView rvResults;

    @BindView(R.id.loading)
    AVLoadingIndicatorView loadingView;

    private NewsAdapter mAdapter;
    private SmartScrollListener mScrollListener;
    private SearchPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this, this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mPresenter = new SearchPresenter(this);
        mPresenter.onCreate(this);


        mAdapter = new NewsAdapter(this, this);
        rvResults.setAdapter(mAdapter);
        rvResults.setLayoutManager(new LinearLayoutManager(this));

        mScrollListener = new SmartScrollListener(new SmartScrollListener.OnSmartScrollListener() {
            @Override
            public void onListEndReached() {
                showLoadMore();
                mPresenter.onResultListEndReached();
            }
        });

        rvResults.addOnScrollListener(mScrollListener);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener()

        {
            @Override
            public boolean onEditorAction(TextView textView, int keyCode, KeyEvent keyEvent) {
                if (keyCode == EditorInfo.IME_ACTION_SEARCH) {
                    if (!etSearch.getText().toString().equals("")) {
                        String query = etSearch.getText().toString();
                        if (!query.equals("")) {
                            mAdapter.clearData();
                            hideSoftKeyboard(getApplicationContext());
                            if (AppUtils.getObjInstance().hasConnection()) {
                                mPresenter.onTapSearch(query);
                                tvMessage.setText("searching...");
                            } else {
                                tvMessage.setVisibility(View.VISIBLE);
                                tvMessage.setText("No internet connection!");
                            }
                        } else {
                            tvMessage.setError("Enter keywords.");
                        }
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void showLoadMore() {
        Snackbar snackbar = Snackbar.make(rvResults, "loading news...", Snackbar.LENGTH_LONG);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }


    @Override
    public void onTapNews(NewsVO news) {
        mPresenter.onTapResult(news.getUrl());
    }

    @Override
    public void displaySearchResults(List<NewsVO> resultList) {
        loadingView.setVisibility(View.GONE);
        tvMessage.setVisibility(View.GONE);
        mAdapter.appendNewData(resultList);
    }

    @Override
    public void showLoding() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void navigateToDetails(String url) {
        Intent intent = DetailsActivity.newIntent(getApplicationContext(), url);
        startActivity(intent);
    }

    @Override
    public void showErrorMsg(String message) {
        loadingView.setVisibility(View.GONE);
        tvMessage.setText(message);
        tvMessage.setVisibility(View.VISIBLE);
    }
}
