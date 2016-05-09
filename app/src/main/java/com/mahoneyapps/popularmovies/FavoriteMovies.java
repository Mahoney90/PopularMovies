package com.mahoneyapps.popularmovies;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brendan on 5/5/2016.
 */
public class FavoriteMovies extends SugarRecord {

    // These will be the columns in our FavoriteMovies table
    long movieId;
    String url;
    boolean is_favorite;

    public FavoriteMovies() {

    }

    public FavoriteMovies(long id, String movieUrl, boolean isFavorite) {
        movieId = id;
        url = movieUrl;
        is_favorite = isFavorite;
    }

    // Pass a movie id in and remove the movie from the list of favorites
    void unfavorite(long movieId) {
        List<FavoriteMovies> fav = FavoriteMovies.find(FavoriteMovies.class, "movie_id = ?", String.valueOf(movieId));
        FavoriteMovies favMovie = fav.get(0);
        favMovie.delete();
    }

    // pass in a movie id and string to the poster url and that movie will get added to the list of favorites
    void makeFavorite(long movieId, String url) {
        FavoriteMovies fav = new FavoriteMovies(movieId, url, true);
        fav.save();
    }

    // pass in a movie id to see if that movie is a favorite
    boolean isFavorite(long movieId) {
        List<FavoriteMovies> movie = FavoriteMovies.find(FavoriteMovies.class, "movie_id = ?", String.valueOf(movieId));
        if (!movie.isEmpty()) {
            FavoriteMovies movie1 = movie.get(0);
            if (movie1.is_favorite) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // Delete all movie entries in the table
    void deleteDBResults() {
        FavoriteMovies.deleteAll(FavoriteMovies.class);
    }

    // List all movie entries that are marked as favorites
    List<String> allFavorites() {
        List<String> urlList = new ArrayList<>();
        List<FavoriteMovies> favMovies = FavoriteMovies.find(FavoriteMovies.class, "isfavorite = ?", "1");
        for (FavoriteMovies movie : favMovies) {
            String url = movie.url;
            ;
            urlList.add(url);
        }
        return urlList;
    }

}
