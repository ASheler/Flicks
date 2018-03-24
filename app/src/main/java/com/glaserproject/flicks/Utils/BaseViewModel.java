package com.glaserproject.flicks.Utils;

import android.arch.lifecycle.ViewModel;

import com.glaserproject.flicks.MyObjects.Movie;

/**
 * ViewModel for MainActivity Persistence
 */

public class BaseViewModel extends ViewModel {

    private Movie[] mMovie;
    private int mCurrentSelection;
    private boolean isFilled;
    private Movie singleMovie;
    private Movie movieDetail;

    //initialize for Main Activity
    public void init (Movie[] movies, int currentSelection){
        this.mMovie = movies;
        this.mCurrentSelection = currentSelection;
        this.isFilled = true;
    }

    public void initSingleMovie(Movie movie){
        this.singleMovie = movie;
    }

    //init for Detail Activity
    public void initMovieDetail (Movie movie){
        this.movieDetail = movie;
        this.isFilled = true;
    }

    public Movie getMovieDetail (){
        return movieDetail;
    }

    public void setMovie (Movie[] movie){
        this.mMovie = movie;
    }

    public Movie[] getMovie() {
        return mMovie;
    }

    public void setCurrentSelection (int currentSelection) {
        this.mCurrentSelection = currentSelection;
    }

    public int getCurrentSelection () {
        return mCurrentSelection;
    }

    public Movie getSingleMovie (){
        return singleMovie;
    }

    //get filled state
    public boolean isFilled (){
        return isFilled;
    }


}
