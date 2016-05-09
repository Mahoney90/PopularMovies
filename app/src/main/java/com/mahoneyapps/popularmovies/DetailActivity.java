package com.mahoneyapps.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brendan on 4/28/2016.
 */
public class DetailActivity extends AppCompatActivity {

    private String mTitle;
    private String mReleaseDate;
    private String mSynopsis;
    private String mUrl;
    private String mBackDropUrl;
    private double mVoteAverage;
    private long mId;
    private final String KEY_FOR_TRAILER_INTENT = "trailer_key";
    private final String PATH_API_KEY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    private List<String> mAuthorList = new ArrayList<>();
    private List<String> mReviewList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerAdapter mAdapter;
    Button trailerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        // Display up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Log.d("detail activity", "da");
            // Start new DetailFragment is savedInstanceState is null
            DetailFragment detailFragment = new DetailFragment();
            getFragmentManager().beginTransaction().add(R.id.detail_container, detailFragment, "DETAIL").commit();
        }

    }
}
