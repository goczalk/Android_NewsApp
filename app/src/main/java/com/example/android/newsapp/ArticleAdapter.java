package com.example.android.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by klaudia on 06/08/18.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, @NonNull List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);
        }

        final Article currentArticle = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentArticle.getTitle());

        TextView sectionTextView = (TextView) convertView.findViewById(R.id.section_text_view);
        sectionTextView.setText(currentArticle.getSectionName());

        TextView authorTextView = (TextView) convertView.findViewById(R.id.author_text_view);
        authorTextView.setText(currentArticle.getAuthor());

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
        dateTextView.setText(currentArticle.getDate());

        return  convertView;
    }
}
