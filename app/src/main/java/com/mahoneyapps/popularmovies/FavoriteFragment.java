package com.mahoneyapps.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brendan on 5/5/2016.
 */
public class FavoriteFragment extends Fragment {

    private final String PATH_API_KEY = "api_key";
    private static final String API_KEY = BuildConfig.API_KEY;
    private final String POSTER_URL_KEY_FOR_FAVORITES = "poster_url";
    private String mPosterUrl;
    List<String> favoriteMovieList;
    MoviePosterAdapter favoriteAdapter;
    private final String INTENT_EXTRA_MOVIES_KEY = "movies_key";

    public interface Callback {
        boolean onFavoriteSelected(Movie movie, int position);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.grid_layout, container, false);

        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            // Set title to "Favorite Movies"
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Favorite Movies");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // New ArrayList to store urls
        List<String> urlFromDetail = new ArrayList<>();

        // Get poster url from Shared Preferences
        mPosterUrl = preferences.getString(POSTER_URL_KEY_FOR_FAVORITES, null);

        // Add poster url to poster ArrayList
        urlFromDetail.add(mPosterUrl);

        // Initialize GridView and an instance of our FavoriteMovies table
        // Query all movies listed as a Favorite
        GridView gridView = (GridView) view.findViewById(R.id.movies_grid);
        FavoriteMovies fav = new FavoriteMovies();
        favoriteMovieList = fav.allFavorites();

        // Pass the context and list of favorite movies into a new MoviePosterAdapter, and set it as the
        // adapter for our GridView
        favoriteAdapter = new MoviePosterAdapter(getActivity(), favoriteMovieList);
        gridView.setAdapter(favoriteAdapter);

        // Set an Item Click Listener on our GridView, when we click on an item, start an intent,
        // passing the Movie object that was clicked as an Intent Extra to our DetailActivity class
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openDetails = new Intent(getActivity(), DetailActivity.class);
                // Create an instance of our Movies table and find the position of the movie
                // clicked in our favorite movies list
                Movies movies = new Movies();
                Movies movieToConvert = movies.findMovie(favoriteMovieList.get(position));

                // Convert this movie to a Movie object
                Movie movie = dbToMovieObject(movieToConvert);
                openDetails.putExtra("MOVIE", movie);

                // If our callback to MainActivity returns false, launch intent to open DetailActivity (1 pane)
                if (!((Callback) getActivity()).onFavoriteSelected(movie, position)) {
                    startActivity(openDetails);
                }
            }
        });

        return view;
    }

    private Movie dbToMovieObject(Movies moviesTableRow) {
        // Convert from a row of data in our table to primitives, to then pass in our Movie constructor
        // and create a new object
        String title = moviesTableRow.title;
        String releaseDate = moviesTableRow.releaseDate;
        String url = moviesTableRow.url;
        String synopsis = moviesTableRow.synopsis;
        String backdropUrl = moviesTableRow.backdropUrl;
        double voteAverage = moviesTableRow.voteAverage;
        long id = moviesTableRow.movieId;

        Movie movie = new Movie(title, releaseDate, url, synopsis, backdropUrl, voteAverage, id);
        return movie;
    }
}
