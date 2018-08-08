package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ArticlesActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String GUARDIAN_URL = "https://content.guardianapis.com/search";

    private static final int ARTICLE_LOADER_ID = 0;
    private ArticleAdapter mAdapter;
    private ProgressBar mLoadingSpinner;
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_activity);

        ListView articleListView = findViewById(R.id.list);
        mEmptyTextView = findViewById(R.id.empty_text_view);
        mLoadingSpinner = findViewById(R.id.loading_spinner);

        if (!isConnectedToInternet()){
            displayNoInternetMsg();
        }
        else {
            mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

            articleListView.setEmptyView(mEmptyTextView);
            articleListView.setAdapter(mAdapter);

            articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Article currentArticle = mAdapter.getItem(position);
                    Uri articleUri = Uri.parse(currentArticle.getUrl());
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                    startActivity(websiteIntent);
                }
            });

            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);
        }
    }

    private void displayNoInternetMsg() {
        mLoadingSpinner.setVisibility(View.GONE);
        mEmptyTextView.setText(R.string.no_internet);
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String keyword = sharedPrefs.getString(
                getString(R.string.settings_search_keyword_key),
                getString(R.string.settings_search_keyword_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(getString(R.string.uri_format), getString(R.string.uri_format_json));
        uriBuilder.appendQueryParameter(getString(R.string.uri_show_tags), getString(R.string.uri_tags_contributor));
        uriBuilder.appendQueryParameter(getString(R.string.uri_keyword), keyword);
        uriBuilder.appendQueryParameter(getString(R.string.uri_order_by), orderBy);
        uriBuilder.appendQueryParameter(getString(R.string.uri_api_key), getString(R.string.uri_my_key));

        return new ArticleLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        if (!isConnectedToInternet()){
            displayNoInternetMsg();
        }
        else{
            mLoadingSpinner.setVisibility(View.GONE);
            mAdapter.clear();

            if (articles != null && !articles.isEmpty()) {
                mAdapter.addAll(articles);
            }

            mEmptyTextView.setText(R.string.no_articles_found);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
