package com.mahoneyapps.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            MovieFragment movieFragment = new MovieFragment();
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.sort_preference_key), "popular");
            movieFragment.setArguments(bundle);
            // open a new MovieFragment (most popular)
            getFragmentManager().beginTransaction().add(R.id.container, movieFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // if user selects "Top Movies" option from menu, open a fragment displaying the top rated movies on TMDB
        if (id == R.id.top_movies){

            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.sort_preference_key), "top_rated");
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.container, movieFragment).commit();

        }
        // if user selects "Most Popular" option from menu, open a fragment displaying the top rated movies on TMDB
        if (id == R.id.most_popular) {
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.sort_preference_key), "popular");
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().add(R.id.container, movieFragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

}
