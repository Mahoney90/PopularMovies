package com.mahoneyapps.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            // open a new MovieFragment (most popular)
            getFragmentManager().beginTransaction().add(R.id.container, new MovieFragment()).commit();
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

        // if user selects "Top Movies" option from menu, open a TopRatedFragment displaying the top rated movies on TMDB
        if (id == R.id.top_movies){
            getFragmentManager().beginTransaction().add(R.id.container, new TopRatedFragment()).commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
