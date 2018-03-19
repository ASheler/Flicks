package com.glaserproject.flicks.Utils;

import android.os.AsyncTask;

import com.glaserproject.flicks.MyObjects.Movie;

import java.net.URL;

/**
 * Load and fetch JSON and get it to Movie[]
 */





public class LoadFetchJSONmovies extends AsyncTask<Movie[], Void, Movie[]> {

    public interface AsyncTaskCompleteListener<T> {
        void onTaskBegin();
        void onTaskComplete(T movies);

    }


    private int currentSelection;
    private AsyncTaskCompleteListener<Movie[]> listener;

    public LoadFetchJSONmovies(int currentSelection, AsyncTaskCompleteListener<Movie[]> listener){
        this.currentSelection = currentSelection;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
        listener.onTaskBegin();
    }

    @Override
    protected Movie[] doInBackground(Movie[]... movies) {

        URL jsonRequestUrl = MovieDbUtils.buildUrl(currentSelection);

        try {
            //get JSON from URL
            String jsonResponse = MovieDbUtils.getJSONFromUrl(jsonRequestUrl);
            //parse data from JSON
            Movie[] movie = MovieDbUtils.parseMovieJSON(jsonResponse);

            return movie;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }

}
