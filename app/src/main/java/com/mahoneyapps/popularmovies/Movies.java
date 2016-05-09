package com.mahoneyapps.popularmovies;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Brendan on 5/6/2016.
 */
public class Movies extends SugarRecord {
    // Columns in Movies database
    String title;
    String releaseDate;
    String url;
    String synopsis;
    String backdropUrl;
    double voteAverage;
    long movieId;

    public Movies() {

    }

    // Constructor when adding a Movie to database, for easy conversion to a Movie object
    public Movies(String movieTitle, String movieReleaseDate, String movieUrl, String movieSynopsis,
                  String movieBackdropUrl, double movieVoteAverage, long id) {
        title = movieTitle;
        releaseDate = movieReleaseDate;
        url = movieUrl;
        synopsis = movieSynopsis;
        backdropUrl = movieBackdropUrl;
        voteAverage = movieVoteAverage;
        movieId = id;
    }

    // Pass in a String url as an argument, find the movie in the database with an equivalent url
    public Movies findMovie(String url) {
        List<Movies> moviesList = Movies.find(Movies.class, "url = ?", url);
        if (moviesList != null) {
            return moviesList.get(0);
        } else {
            return null;
        }

    }
}
