package com.mahoneyapps.popularmovies;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brendan on 4/28/2016.
 */
public class MoviePosterAdapter extends ArrayAdapter {

    List<String> mUrls;
    Context mContext;

    public MoviePosterAdapter(Activity context, List<String> urls) {
        // Pass 0 as middle argument, don't need a resource layout
        super(context, 0, urls);

        // Initialize context and list of URLs that contain paths to movie posters
        mContext = context;
        mUrls = urls;
    }

    @Override
    public int getCount() {
        return mUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        // If a view hasn't been used yet, inflate our list item view to be used and recycled
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_movie_poster, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.poster);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        ImageView posterIcon = (ImageView) convertView.findViewById(R.id.poster);

        // As long as there are URLs passed in our list, use Picasso to load the URLs into our ImageView
        if (mUrls.size() > 0) {
            Picasso.with(mContext).load(mUrls.get(position)).into(holder.imageView);
        }

        return convertView;
    }

    public static class ViewHolder{
        public ImageView imageView;

    }

    public void updateMovies(ArrayList<String> movieUrls){
        mUrls = movieUrls;
        notifyDataSetChanged();
    }
}
