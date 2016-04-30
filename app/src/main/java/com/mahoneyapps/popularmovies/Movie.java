package com.mahoneyapps.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Brendan on 4/29/2016.
 */
public class Movie implements Parcelable {
    String mTitle;
    String mReleaseDate;
    String mUrlPoster;
    String mSynopsis;
    String mBackDropUrl;
    double mVoteAverage;

    // constructor with data from the movie database api
    public Movie(String title, String releaseDate, String urlPoster, String synopsis, String backDropUrl, double voteAverage){
        this.mTitle = title;
        this.mReleaseDate = releaseDate;
        this.mUrlPoster = urlPoster;
        this.mSynopsis = synopsis;
        this.mBackDropUrl = backDropUrl;
        this.mVoteAverage = voteAverage;
    }

    // our constructor with the parcelable as an argument
    public Movie(Parcel parcel){
        this.mTitle = parcel.readString();
        this.mReleaseDate = parcel.readString();
        this.mUrlPoster = parcel.readString();
        this.mSynopsis = parcel.readString();
        this.mBackDropUrl = parcel.readString();
        this.mVoteAverage = parcel.readDouble();
    }


    public Movie(){

    }

    // getters and setters
    private String getTitle(){
        return mTitle;
    }

    private void setTitle(String title){
        this.mTitle = title;
    }

    private String getReleaseDate(){
        return mReleaseDate;
    }

    private void setReleaseDate(String releaseDate){
        this.mReleaseDate = releaseDate;
    }

    private String getPosterUrl(){
        return mUrlPoster;
    }

    private void setPosterUrl(String url){
        this.mUrlPoster = url;
    }

    private String getSynopsis(){
        return mSynopsis;
    }

    private void setSynopsis(String synopsis){
        this.mSynopsis = synopsis;
    }

    private String getBackDropUrl(){
        return mBackDropUrl;
    }

    private void setBackDropUrl(String backDropUrl){
        this.mBackDropUrl = backDropUrl;
    }

    private double getVoteAverage(){
        return mVoteAverage;
    }

    private void setVoteAverage(double voteAverage){
        this.mVoteAverage = voteAverage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // how we flatten our movie object into a parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mUrlPoster);
        dest.writeString(mSynopsis);
        dest.writeString(mBackDropUrl);
        dest.writeDouble(mVoteAverage);
    }

    // generates instances of parcelable class from a parcel
    public static Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
