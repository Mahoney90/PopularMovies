package com.mahoneyapps.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Brendan on 5/5/2016.
 */
public class FavoriteActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        // Display up button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Start new DetailFragment is savedInstanceState is null
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            getFragmentManager().beginTransaction().add(R.id.detail_container, favoriteFragment, "FAVORITE").commit();

        }
    }
}
