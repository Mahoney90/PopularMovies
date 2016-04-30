package com.mahoneyapps.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brendan on 4/29/2016.
 */
public class TopRatedFragment extends Fragment {

    GridView mGridView;
    MoviePosterAdapter mAdapter;
    List<String> mMovieList;
    String[] mUrls = new String[16];
    List<Movie> mPopMovies;
    final String INTENT_EXTRA_MOVIE_KEY = "MOVIE";
    private static final String API_KEY = BuildConfig.API_KEY;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate our GridView
        View rootView = inflater.inflate(R.layout.grid_layout, container, false);

        // Check that getSupportActionBar doesn't return null, and then change the Activity's Title
        if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Top Rated Movies");
        }

        // Initialize ArrayList and our GridView
        mMovieList = new ArrayList<>();
        mGridView = (GridView) rootView.findViewById(R.id.movies_grid);

        // Initialize custom adapter, passing it context and ArrayList of Strings
        mAdapter = new MoviePosterAdapter(getActivity(), mMovieList);
        mGridView.setAdapter(mAdapter);

        // Initialize ArrayList of Movie objects
        mPopMovies = new ArrayList<>();

        // Set an Item Click Listener on our GridView, when we click on an item, start an intent,
        // passing the Movie object that was clicked as an Intent Extra to our DetailActivity class
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openDetails = new Intent(getActivity(), DetailActivity.class);
                Movie movie = mPopMovies.get(position);
                openDetails.putExtra(INTENT_EXTRA_MOVIE_KEY, movie);
                startActivity(openDetails);
            }
        });

        // Execute our Async Task
        new FetchMovieTitles().execute();

        return rootView;
    }

    class FetchMovieTitles extends AsyncTask<Void, Void, List<String>> {

        // Variables for URI Builder
        private final String BASE_URL = "api.themoviedb.org";
        private final String PATH_VERSION = "3";
        private final String PATH_MOVIE = "movie";
        private final String PATH_TOP_RATED = "top_rated";
        private final String PATH_API_KEY = "api_key";
        private final String MY_API = getString(R.string.tmdb_api);

        @Override
        protected List<String> doInBackground(Void... params) {

            // Declare some variables outside of the try/catch block so we can access them later
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            URL url;
            String movieJSONString = null;

            // URL for Highest User Rated Movies API service
            try {
                Uri.Builder uri = new Uri.Builder();
                uri.scheme("http").
                        authority(BASE_URL).
                        appendPath(PATH_VERSION).
                        appendPath(PATH_MOVIE).
                        appendPath(PATH_TOP_RATED).
                        appendQueryParameter(PATH_API_KEY, API_KEY);

                String popularUrl = uri.build().toString();
                url = new URL(popularUrl);

                // Pass our string to a URL and then open a connection to the URL
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Get input stream on our connection and initialize a StringBuffer to read through and build Strings
                InputStream movieInput = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                // If our input stream is null, return null so as to not waste resources by parsing
                if (movieInput == null) {
                    return null;
                }

                // Create a new BufferedReader, taking our input stream from our URL connection as input
                reader = new BufferedReader(new InputStreamReader(movieInput));
                String line;

                // While there are lines to parse, continue reading the input stream,
                // appending lines (on new lines)
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // input was empty, don't waste resources parsing
                    return null;
                }

                // Convert the data we read from the input stream to a String
                movieJSONString = buffer.toString();

            } catch (IOException i){
                i.printStackTrace();
            }

            finally {
                // If we had previously established a URL connection, disconnect now
                if (urlConnection != null){
                    urlConnection.disconnect();
                }
                // Also close our BufferedReader
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }



            try {
                // Pass JSON String to getMovieDataFromJSON method and return the result, a List<String>
                return getMovieDataFromJSON(movieJSONString);
            } catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<String> urls) {
            // If our List<String> of URLs passed from doInBackground isn't null, add the List to
            // our custom adapter and reset the Adapter on the GridView so it updates with our Movie URL data
            if (urls != null){
                Log.d("post execute NOT NULL", String.valueOf(urls));
                MoviePosterAdapter moviePosterAdapter = new MoviePosterAdapter(getActivity(), urls);
                mGridView.setAdapter(moviePosterAdapter);

            }
        }

        private List<String> getMovieDataFromJSON(String movieJSONString) throws JSONException{

            // Initialize constants to query TMDB API
            final String RESULTS = "results";
            final String TITLE = "title";
            final String POSTER_PATH = "poster_path";
            final String RELEASE_DATE = "release_date";
            final String VOTE_AVERAGE = "vote_average";
            final String PLOT_SYNOPSIS = "overview";
            final String BACKDROP_URL = "backdrop_path";
            String url = "";

            // Parse our JSON by initially getting a JSONObject, and then looking for the results array
            JSONObject object = new JSONObject(movieJSONString);
            JSONArray resultsArray = object.getJSONArray(RESULTS);

            // Make an array of Movies with a size of 16
            Movie[] movieResultsStr = new Movie[16];

            // Loop through the results array to return the first 16 movies
            for (int i = 0; i < movieResultsStr.length; i++){
                JSONObject movieObject = resultsArray.getJSONObject(i);
                String posterPath = movieObject.getString(POSTER_PATH);

                // Use URI Builder to build URL string for Highest Rated API call
                Uri.Builder uri = new Uri.Builder();
                uri.scheme("http").
                        authority("image.tmdb.org").
                        appendPath("t").
                        appendPath("p").
                        appendPath("w185").
                        appendEncodedPath(posterPath).
                        build();

                url = uri.toString();

                // Add URL poster String to our Movie List
                mMovieList.add(url);

                // Add URL poster string to our array of URL Strings
                mUrls[i] = url;

                // Retrieving data about Movie object to later be passed to DetailsActivity page
                String movieTitle = movieObject.getString(TITLE);
                String movieReleaseDate = movieObject.getString(RELEASE_DATE);
                String movieSynopsis = movieObject.getString(PLOT_SYNOPSIS);
                String movieBackDrop = movieObject.getString(BACKDROP_URL);
                double movieVoteAverage = movieObject.getDouble(VOTE_AVERAGE);

                // Using URI Builder to build URL string for Movie backdrop picture, increasing size to w342
                Uri.Builder uriBackDrop = new Uri.Builder();
                uriBackDrop.scheme("http").
                        authority("image.tmdb.org").
                        appendPath("t").
                        appendPath("p").
                        appendPath("w342").
                        appendEncodedPath(movieBackDrop).
                        build();

                String backDropURL = uriBackDrop.toString();

                // Create new Movie object, passing in all pertinent data retrieved from JSON parsing
                Movie movie = new Movie(movieTitle, movieReleaseDate, url, movieSynopsis, backDropURL, movieVoteAverage);

                // Add a copy of that Movie object to the proper position in our array
                movieResultsStr[i] = movie;

                // Add movie to our List<Movie> mPopMovies, used for our DetailActivity pag
                mPopMovies.add(movie);
            }

            // pass string array or poster path URLs to an ArrayList, which is passed through to onPostExecute
            // and eventually passed as a parameter in our Custom Adapter to populate the GridView
            mMovieList = new ArrayList<>(Arrays.asList(mUrls));
            return mMovieList;

        }
    }
}

