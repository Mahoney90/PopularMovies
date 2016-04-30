package com.mahoneyapps.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize views to populate with data retrieved from movie database API
        TextView titleText = (TextView) findViewById(R.id.movie_title);
        TextView releaseDateText = (TextView) findViewById(R.id.movie_release_date);
        TextView synopsisText = (TextView) findViewById(R.id.synopsis);
        TextView ratingText = (TextView) findViewById(R.id.voter_rating);
        ImageView backDropImage = (ImageView) findViewById(R.id.backdrop_image);

        // retrieve parcelable from fragment class, containing Movie object with data
        Movie movie = (Movie) getIntent().getParcelableExtra("MOVIE");

        // initialize our member variables with previously set variables from our parcelable Movie objects
        mTitle = movie.mTitle;
        mReleaseDate = movie.mReleaseDate;
        mUrl = movie.mUrlPoster;
        mSynopsis = movie.mSynopsis;
        mBackDropUrl = movie.mBackDropUrl;
        mVoteAverage = movie.mVoteAverage;

        // set texts on our views, populating with data from the user selected movie
        titleText.setText(mTitle);
        releaseDateText.setText("Release date: " + mReleaseDate);
        synopsisText.setText(mSynopsis);
        ratingText.setText(mVoteAverage + "/10");

        // using backdrop image for the DetailActivity
        Picasso.with(this).load(mBackDropUrl).into(backDropImage);

    }
}
