package com.glaserproject.flicks.Movie;

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

    public Movie (){
    }

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

}
