package com.glaserproject.flicks.MyObjects;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Movie Object
 */

public class Movie implements Parcelable{

    private int id;
    private int voteCount;
    private String voteAverage;
    private String movieTitle;
    private String popularity;
    private String posterPath;
    private String origLang;
    private String origTitle;
    private List<Integer> genres;
    private String backdropPath;
    private boolean adult;
    private String overview;
    private String releaseDate;
    private String tagline;
    private int revenue;
    private int budget;
    private Trailer[] trailers;
    private Review[] reviews;


    public Movie() {
    }

    //full movie
    public Movie(int id, int voteCount, String voteAverage, String movieTitle, String popularity,
                 String posterPath, String origLang, String origTitle, List<Integer> genres, String backdropPath,
                 boolean adult, String overview, String releaseDate) {

        this.id = id;
        this.voteCount = voteCount;
        this.voteAverage = voteAverage;
        this.movieTitle = movieTitle;
        this.popularity = popularity;
        this.posterPath = posterPath;
        this.origLang = origLang;
        this.origTitle = origTitle;
        this.genres = genres;
        this.backdropPath = backdropPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
    }

    //Movie Detail
    public Movie(String tagline, int revenue, String overview, int budget, String popularity, String voteAverage) {
        this.tagline = tagline;
        this.revenue = revenue;
        this.overview = overview;
        this.budget = budget;
        this.popularity = popularity;
        this.voteAverage = voteAverage;

    }


    //getters
    public int getId() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getPopularity() {
        return popularity;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOrigLang() {
        return origLang;
    }

    public String getOrigTitle() {
        return origTitle;
    }

    public List<Integer> getGenres() {
        return genres;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public boolean getAdult() {
        return adult;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getTagline() {
        return tagline;
    }

    public int getRevenue() {
        return revenue;
    }

    public int getBudget() {
        return budget;
    }

    public void putTrailer (Trailer[] trailer){
        this.trailers = trailer;
    }

    public Trailer[] getTrailers () {
        return trailers;
    }

    public void putReview (Review[] reviews){
        this.reviews = reviews;
    }

    public Review[] getReviews (){
        return reviews;
    }




    //make parcelable for passing through activity
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(backdropPath);
        dest.writeString(movieTitle);
        dest.writeString(releaseDate);
    }

    //Parcel IN
    private Movie (Parcel in){
        id = in.readInt();
        backdropPath = in.readString();
        movieTitle = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }


    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>(){

                @Override
                public Movie createFromParcel(Parcel source) {
                    //through parc. IN - create new Movie object
                    return new Movie(source);
                }

                @Override
                public Movie[] newArray(int size) {
                    //get size
                    return new Movie[size];
                }
            };
}
