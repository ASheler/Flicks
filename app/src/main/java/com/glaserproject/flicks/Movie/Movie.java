package com.glaserproject.flicks.Movie;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Movie Object
 */

public class Movie {

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


    public Movie (){
    }
    //full movie
    public Movie (int id, int voteCount, String voteAverage, String movieTitle, String popularity,
                  String posterPath, String origLang, String origTitle, List<Integer> genres, String backdropPath,
                  boolean adult, String overview, String releaseDate){

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
    public Movie (String tagline, int revenue, String overview, int budget, String popularity, String voteAverage){
        this.tagline = tagline;
        this.revenue = revenue;
        this.overview = overview;
        this.budget = budget;
        this.popularity = popularity;
        this.voteAverage = voteAverage;

    }




    public int getId (){
        return id;
    }
    public int getVoteCount (){
        return voteCount;
    }
    public String getVoteAverage (){
        return voteAverage;
    }
    public String getMovieTitle (){
        return movieTitle;
    }
    public String getPopularity (){
        return popularity;
    }
    public String getPosterPath (){
        return posterPath;
    }
    public String getOrigLang (){
        return origLang;
    }
    public String getOrigTitle (){
        return origTitle;
    }
    public List<Integer> getGenres (){
        return genres;
    }
    public String getBackdropPath (){
        return backdropPath;
    }
    public boolean getAdult (){
        return adult;
    }
    public String getOverview (){
        return overview;
    }
    public String getReleaseDate (){
        return releaseDate;
    }
    public String getTagline (){
        return tagline;
    }
    public int getRevenue (){
        return revenue;
    }
    public int getBudget (){
        return budget;
    }
}
