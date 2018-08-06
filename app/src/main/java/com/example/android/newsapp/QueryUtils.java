package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by klaudia on 06/08/18.
 */

public class QueryUtils {
    private static final String LOG_TAG = "QueryUtils";

    private QueryUtils() {
    }

    public static List<Article> extractArticles(String jsonResponse) {
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }

        List<Article> articles = new ArrayList<>();

        try {
            JSONObject jsonRootObject = new JSONObject(jsonResponse);
            JSONObject jsonResponseObject = jsonRootObject.getJSONObject("response");
            JSONArray jsonResultsArray = jsonResponseObject.getJSONArray("results");

            for(int i = 0; i < jsonResultsArray.length(); i++){
                JSONObject jsonResultObject = jsonResultsArray.getJSONObject(i);

                String title = jsonResultObject.getString("webTitle");
                String sectionName = jsonResultObject.getString("sectionName");
                String date = jsonResultObject.getString("webPublicationDate");
                String url = jsonResultObject.getString("webUrl");

                JSONArray jsonTagsArray = jsonResultObject.getJSONArray("tags");
                JSONObject jsonFirstTagObject = jsonTagsArray.getJSONObject(0);
                String author = jsonFirstTagObject.getString("webTitle");

                articles.add(new Article(title, sectionName, author, url, date));
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return articles;
    }

}
