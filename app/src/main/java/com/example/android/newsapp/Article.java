package com.example.android.newsapp;

/**
 * Created by klaudia on 06/08/18.
 */


//mTitle -> Hungarnian Notations, not recommendedn anymore
public class Article {
    private String mTitle;
    private String mSectionName;
    private String mAuthor;
    private String mUrl;
    private String mDate;

    public Article(String mTitle, String mSectionName, String mAuthor, String mUrl, String mDate) {
        this.mTitle = mTitle;
        this.mSectionName = mSectionName;
        this.mAuthor = mAuthor;
        this.mUrl = mUrl;
        this.mDate = mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getDate() {
        return mDate;
    }
}
