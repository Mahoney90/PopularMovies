package com.mahoneyapps.popularmovies;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Brendan on 5/3/2016.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int EMPTY_VIEW = 10;
    private List<String> mAuthors;
    private List<String> mReviews;

    public class EmptyViewHolder extends RecyclerView.ViewHolder{
        public EmptyViewHolder(View itemView){
            super(itemView);
        }
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView authorView, reviewView;

        public ReviewHolder(View itemView) {
            super(itemView);
            // Initialize TextViews to display Author name and Review name
            authorView = (TextView) itemView.findViewById(R.id.author_item);
            reviewView = (TextView) itemView.findViewById(R.id.review_material);
        }
    }

    public RecyclerAdapter(List<String> authorList, List<String> reviewList){
        // Initialize ArrayLists with arguments passed in to RecyclerAdapter
        mAuthors = authorList;
        mReviews = reviewList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if (i == EMPTY_VIEW){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.empty_view, viewGroup, false);
            EmptyViewHolder emptyHolder = new EmptyViewHolder(view);
            return emptyHolder;
        } else {
            // Inflate review_item layout and pass it into our ReviewHolder
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item, viewGroup, false);
            ReviewHolder reviewHolder = new ReviewHolder(view);
            return reviewHolder;
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder reviewHolder, int i) {
        if (reviewHolder instanceof ReviewHolder){
            // Set Author and Review text of appropriate Author/Review from ArrayList if
            // the reviewholder parameter is an instance of ReviewHolder (and not EmptyHolder)
            ((ReviewHolder) reviewHolder).authorView.setText(mAuthors.get(i));
            ((ReviewHolder) reviewHolder).reviewView.setText(mReviews.get(i));
        }

    }

    @Override
    public int getItemViewType(int position) {
        // If our ArrayList of authors is 0, return EMPTY_VIEW and inflate our Empty View layout
        if (mAuthors.size() == 0){
            return EMPTY_VIEW;
        }
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        // return size of Author ArrayList, unless it is empty, then return 1
        return mAuthors.size() > 0 ? mAuthors.size() : 1;
    }

}
