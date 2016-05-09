package com.mahoneyapps.popularmovies;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements MovieFragment.Callback, FavoriteFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            Log.d("is 2 pane", "2 pane");
            mTwoPane = true;
            if (savedInstanceState == null) {
                Log.d("is 2 pane null", "null");
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.sort_preference_key), "popular");
                MovieFragment movieFragment = new MovieFragment();
                movieFragment.setArguments(bundle);
                getFragmentManager().beginTransaction().
                        replace(R.id.movie_fragment, movieFragment, DETAILFRAGMENT_TAG).commit();
                Log.d("still here 2 pane", "second one");

                getFragmentManager().beginTransaction().replace(R.id.detail_container, new DetailFragment()).commit();
            }
        } else {
            mTwoPane = false;
            if (savedInstanceState == null) {
                MovieFragment movieFragment = new MovieFragment();
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.sort_preference_key), "popular");
                movieFragment.setArguments(bundle);

                // open a new MovieFragment (most popular)
                getFragmentManager().beginTransaction().add(R.id.movie_fragment, movieFragment).commit();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MovieFragment movieFragment = (MovieFragment) getFragmentManager().findFragmentById(R.id.movie_fragment);
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
        if (id == R.id.top_movies) {
            // If the Top Movies setting is clicked, make appropriate API call in MovieFragment
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.sort_preference_key), "top_rated");
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.movie_fragment, movieFragment).commit();

        }
        if (id == R.id.most_popular) {
            // If the Most Popular setting is clicked, make appropriate API call in MovieFragment
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.sort_preference_key), "popular");
            MovieFragment movieFragment = new MovieFragment();
            movieFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.movie_fragment, movieFragment).commit();
        }
        if (id == R.id.favorite_movies) {
            // If the Favorite setting is clicked, query database for movie favorites
            Bundle bundle = new Bundle();
            bundle.putString(getString(R.string.sort_preference_key), "favorite");
            FavoriteFragment favFragment = new FavoriteFragment();
            favFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.movie_fragment, favFragment).commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemSelected(Movie movie, int position) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("MOVIE", movie);
            bundle.putInt("POSITION", position);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();
            return true;
        }
        return false;
    }


    @Override
    public boolean onFavoriteSelected(Movie movie, int position) {
        if (mTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putParcelable("MOVIE", movie);
            bundle.putInt("POSITION", position);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            getFragmentManager().beginTransaction().replace(R.id.detail_container, detailFragment, DETAILFRAGMENT_TAG).commit();
            return true;
        }
        return false;
    }
}
