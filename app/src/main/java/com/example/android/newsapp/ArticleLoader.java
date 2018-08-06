package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by klaudia on 06/08/18.
 */

class ArticleLoader extends AsyncTaskLoader<List<Article>> {
    private final String mUrl;

    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    public List<Article> loadInBackground() {
        if(TextUtils.isEmpty(mUrl)){
            return  null;
        }

        List<Article> articles = QueryUtils.fetchArticlesData(mUrl);
        return articles;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
