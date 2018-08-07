package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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
                String url = jsonResultObject.getString("webUrl");
                String date = jsonResultObject.optString("webPublicationDate");

                JSONArray jsonTagsArray = jsonResultObject.optJSONArray("tags");
                String author = "";
                if(jsonTagsArray != null) {
                    JSONObject jsonFirstTagObject = jsonTagsArray.optJSONObject(0);
                    if (jsonFirstTagObject != null) {
                        author = jsonFirstTagObject.optString("webTitle");
                    }
                }
                articles.add(new Article(title, sectionName, author, url, date));
            }


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the articles JSON results", e);
        }
        return articles;
    }

    public static List<Article> fetchArticlesData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        List<Article> articles = QueryUtils.extractArticles(jsonResponse);

        return articles;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the articles JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
}
