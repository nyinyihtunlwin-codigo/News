package com.nyinyihtunlwin.news.acitivities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.nyinyihtunlwin.news.R;
import com.nyinyihtunlwin.news.adapters.NewsAdapter;
import com.nyinyihtunlwin.news.components.SmartRecyclerView;
import com.nyinyihtunlwin.news.components.SmartScrollListener;
import com.nyinyihtunlwin.news.data.vos.NewsVO;
import com.nyinyihtunlwin.news.data.vos.SourceVO;
import com.nyinyihtunlwin.news.delegates.NewsItemDelegate;
import com.nyinyihtunlwin.news.mvp.presenters.SearchPresenter;
import com.nyinyihtunlwin.news.mvp.views.SearchView;
import com.nyinyihtunlwin.news.persistence.NewsContract;
import com.nyinyihtunlwin.news.utils.AppConstants;
import com.nyinyihtunlwin.news.utils.AppUtils;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchActivity extends BaseActivity implements NewsItemDelegate, SearchView, LoaderManager.LoaderCallbacks<Cursor> {

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

    @BindView(R.id.sp_sources)
    AppCompatSpinner spSources;

    private NewsAdapter mAdapter;
    private SmartScrollListener mScrollListener;
    private SearchPresenter mPresenter;

    private String source, query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this, this);
        source = "";
        query = "";

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
                        query = etSearch.getText().toString();
                        if (!query.equals("")) {
                            mAdapter.clearData();
                            hideSoftKeyboard(getApplicationContext());
                            if (AppUtils.getObjInstance().hasConnection()) {
                                mPresenter.onTapSearch(query, source);
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

        getSupportLoaderManager().initLoader(AppConstants.SOURCES_LOADER_ID, null, SearchActivity.this);
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
    public void displaySources(List<SourceVO> sourceList) {
        final List<SourceVO> sources = new ArrayList<>();
        sources.add(new SourceVO());
        List<String> sourceNames = new ArrayList<>();
        sourceNames.add("All");
        for (SourceVO sourceVO : sourceList) {
            if (sourceVO.getId() != null) {
                sources.add(sourceVO);
                sourceNames.add(sourceVO.getName());
            }
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sourceNames);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spSources.setAdapter(dataAdapter);
        spSources.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    source = "";
                } else {
                    source = sources.get(i).getId();
                }
                mAdapter.clearData();
                hideSoftKeyboard(getApplicationContext());
                if (AppUtils.getObjInstance().hasConnection()) {
                    mPresenter.onTapSearch(query, source);
                    tvMessage.setText("searching...");
                } else {
                    tvMessage.setVisibility(View.VISIBLE);
                    tvMessage.setText("No internet connection!");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getApplicationContext(),
                NewsContract.SourceEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mPresenter.onDataLoaded(getApplicationContext(), data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
