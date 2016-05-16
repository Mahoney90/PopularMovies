package com.mahoneyapps.popularmovies;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;

/**
 * Created by Brendan on 5/3/2016.
 */
public class DetailFragment extends Fragment {

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
    TextView reviewView;
    TextView authorView;
    RecyclerView.LayoutManager llm;
    ImageButton mOpenTrailer;
    ImageButton mStarFavorite;
    Button mShowReview;
    Button mHideReview;
    private int counter;
    private final String POSTER_URL_KEY_FOR_FAVORITES = "poster_url";
    List<String> favoriteMovieList;
    MoviePosterAdapter favoriteAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.movie_details, container, false);

        ButterKnife.bind(this, view);

        // retrieve parcelable from fragment class, containing Movie object with data
        Bundle args = getArguments();
        Intent i = getActivity().getIntent();
        if (args != null) {
            // If args isn't null, attempt to get movie parcelable
            Movie movie = (Movie) args.getParcelable("MOVIE");
            fillInMovieData(movie, view);
        } else if (i != null) {
            // If movie is null, then we get our parcelable from DetailActivity intent
            Movie movie = i.getParcelableExtra("MOVIE");
            fillInMovieData(movie, view);
        }

        return view;
    }

    private void fillInMovieData(Movie movie, final View view) {

        // If movie isn't null, fill in DetailsFragment with movie details
        if (movie != null) {
            // initialize our member variables with previously set variables from our parcelable Movie objects
            mTitle = movie.mTitle;
            mReleaseDate = movie.mReleaseDate;
            mUrl = movie.mUrlPoster;
            mSynopsis = movie.mSynopsis;
            mBackDropUrl = movie.mBackDropUrl;
            mVoteAverage = movie.mVoteAverage;
            mId = movie.mId;

            // initialize views to populate with data retrieved from movie database API
            TextView titleText = (TextView) view.findViewById(R.id.movie_title);
            TextView releaseDateText = (TextView) view.findViewById(R.id.movie_release_date);
            TextView synopsisText = (TextView) view.findViewById(R.id.synopsis);
            TextView ratingText = (TextView) view.findViewById(R.id.voter_rating);
            ImageView backDropImage = (ImageView) view.findViewById(R.id.backdrop_image);
            mStarFavorite = (ImageButton) view.findViewById(R.id.favorite);
            mOpenTrailer = (ImageButton) view.findViewById(R.id.play_button);

            // When user clicks on Play button, launch watchVideo method
            mOpenTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    watchVideo();
                }
            });

            mStarFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // check to see if it's a favorite, if true, unfavorite on click - if false, favorite on click
                    if (handleFavoriteButton()) {
                        mStarFavorite.setImageResource(R.drawable.star_off);
                        FavoriteMovies fav = new FavoriteMovies();
                        fav.unfavorite(mId);
                    } else {
                        mStarFavorite.setImageResource(R.drawable.star);
                        Toast.makeText(getActivity(), R.string.saved, Toast.LENGTH_SHORT).show();
                        FavoriteMovies fav = new FavoriteMovies();
                        fav.makeFavorite(mId, mUrl);
                    }
                }
            });

            // Set layoutmanager on recycylerview and RecyclerAdapter as adapter on recyclerview,
            // passing list of Authors and Reviews to adapter
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            llm = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(llm);
            mAdapter = new RecyclerAdapter(mAuthorList, mReviewList);
            recyclerView.setAdapter(mAdapter);

            favoriteMovieList = new ArrayList<>();

            handleFavoriteButton();

            fetchTrailer(mId);


            // set texts on our views, populating with data from the user selected movie
            titleText.setText(mTitle);
            releaseDateText.setText("Release date: " + mReleaseDate);
            synopsisText.setText(mSynopsis);
            ratingText.setText(mVoteAverage + "/10");

            // using backdrop image for the DetailActivity
            Picasso.with(getActivity()).load(mBackDropUrl).into(backDropImage);

            // Show reviews and change text to "Hide Reviews"
            mShowReview = (Button) view.findViewById(R.id.show_reviews);
            mShowReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showReviews(view);
                }
            });

            // Hide reviews and change text to "Show Reviews"
            mHideReview = (Button) view.findViewById(R.id.hide_reviews);
            mHideReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideReview(view);
                }
            });
        }
    }

    private boolean handleFavoriteButton() {

        // if the Movie passed is a favorite,
        FavoriteMovies favs = new FavoriteMovies();
        if (favs.isFavorite(mId)) {
            if (mUrl != null) {
                favoriteMovieList.add(mUrl);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(POSTER_URL_KEY_FOR_FAVORITES, mUrl);
                editor.apply();
            }

            mStarFavorite.setImageResource(R.drawable.star);
            return true;
        } else {
            mStarFavorite.setImageResource(R.drawable.star_off);
            return false;
        }
    }

    public List<String> favoriteMovies() {
        return favoriteMovieList;
    }

    private void hideReview(View view) {
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler.setVisibility(View.GONE);
        mHideReview.setVisibility(View.GONE);
        mShowReview.setVisibility(View.VISIBLE);
    }

    private void showReviews(View view) {
        RecyclerView recycler = (RecyclerView) view.findViewById(R.id.recycler_view);
        recycler.setVisibility(View.VISIBLE);

        mShowReview.setVisibility(View.GONE);
        mHideReview.setVisibility(View.VISIBLE);
    }

    private void fetchTrailer(long movieId) {

        final String REVIEWS = "reviews";
        final String TOTAL_REVIEW_RESULTS = "total_results";
        final String AUTHOR = "author";
        final String CONTENT = "content";
        final String RESULTS_ARRAY = "results";

        // Build URI to access reviews and trailer path for movies
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http").
                authority("api.themoviedb.org").
                appendPath("3").
                appendPath("movie").
                appendPath(String.valueOf(movieId)).
                appendQueryParameter(PATH_API_KEY, API_KEY).
                appendQueryParameter("append_to_response", "trailers,reviews");

        String trailerReviewUrl = builder.build().toString();

        // Make Json request to the API, parse Json to get trailer path and review information
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, trailerReviewUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject object) {
                try {
                    JSONObject trailersObject = object.getJSONObject("trailers");
                    JSONArray youtubeArray = trailersObject.getJSONArray("youtube");
                    JSONObject firstTrailerObject = youtubeArray.getJSONObject(0);
                    String trailerPath = firstTrailerObject.getString("source");

                    // Put trailer path into Shared Preferences
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(KEY_FOR_TRAILER_INTENT, trailerPath).apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("Something went wrong");
                volleyError.printStackTrace();
            }
        });

        // Make another Json request to get review content (author and review message)
        JsonObjectRequest jsonreviewRequest = new JsonObjectRequest(Request.Method.GET, trailerReviewUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    JSONObject reviewObject = jsonObject.getJSONObject(REVIEWS);
                    int numberResults = reviewObject.getInt(TOTAL_REVIEW_RESULTS);

                    JSONArray resultsArray = reviewObject.getJSONArray(RESULTS_ARRAY);
                    for (int i = 0; i < numberResults; i++) {
                        JSONObject resultsObject = resultsArray.getJSONObject(i);
                        String reviewAuthor = resultsObject.getString(AUTHOR);
                        String reviewMessage = resultsObject.getString(CONTENT);

                        // Pass author and message into method to add to ArrayLists and update adapter
                        makeReviewArray(reviewAuthor, reviewMessage);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        Volley.newRequestQueue(getActivity()).add(jsonreviewRequest);
        Volley.newRequestQueue(getActivity()).add(jsonRequest);

    }


    private void makeReviewArray(String reviewAuthor, String reviewMessage) {
        final String AUTHOR_LIST_KEY = "author_list";
        final String REVIEW_LIST_KEY = "review_list";

        // Add Author and Message to respective Lists
        mAuthorList.add(reviewAuthor);
        mReviewList.add(reviewMessage);

        Set<String> setAuthors = new HashSet<>();
        setAuthors.addAll(mAuthorList);

        Set<String> setReviews = new HashSet<>();
        setAuthors.addAll(setReviews);

        // Commit list of Authors and Reviews to SharedPreferences
        if (isAdded()){
            // Simple check to see if the Fragment has been added to the Activity, so that our getActivity
            // call doesn't return null
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet(AUTHOR_LIST_KEY, setAuthors).apply();

            SharedPreferences preferences2 = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor editor2 = preferences2.edit();
            editor2.putStringSet(REVIEW_LIST_KEY, setReviews).apply();
        }

        // Notify adapter that lists of Authors and Reviews have changed
        mAdapter.notifyDataSetChanged();
    }


    public void watchVideo() {
        // Launch intent to load trailer in youtube
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String trailerPath = preferences.getString("trailer_key", "default");
        String youtube = "http://www.youtube.com/watch?v=";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(youtube + trailerPath));
        startActivity(intent);
    }

}
