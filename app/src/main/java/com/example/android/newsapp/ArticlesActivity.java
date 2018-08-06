package com.example.android.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticlesActivity extends AppCompatActivity {

    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_activity);

        ArrayList<Article> articles = new ArrayList<Article>();
//        articles.add(new Article("a", "a","a","a","a"));
//        articles.add(new Article("a", "a","a","a","a"));
//        articles.add(new Article("a", "a","a","a","a"));
//        articles.add(new Article("a", "a","a","a","a"));

        mAdapter = new ArticleAdapter(this, articles);

        ListView articleListView = (ListView) findViewById(R.id.list);
        TextView mEmptyTextView = (TextView) findViewById(R.id.empty_text_view);

        articleListView.setEmptyView(mEmptyTextView);
        articleListView.setAdapter(mAdapter);

        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article currentEarthquake = mAdapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
    }
}
