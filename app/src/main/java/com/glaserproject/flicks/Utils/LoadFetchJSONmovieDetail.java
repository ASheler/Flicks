package com.glaserproject.flicks.Utils;

import android.os.AsyncTask;

import com.glaserproject.flicks.Movie.Movie;

import java.net.URL;

/**
 * Fetch and load JSOM wit Movie details
 */

public class LoadFetchJSONmovieDetail extends AsyncTask<Movie, Void, Movie> {

    public interface AsyncTaskCompleteListener<T> {
        void onTaskBegin();
        void onTaskComplete(T movie);
    }


    private int movieID;
    private AsyncTaskCompleteListener<Movie> listener;

    public LoadFetchJSONmovieDetail(int movieID, AsyncTaskCompleteListener<Movie> listener){
        this.movieID = movieID;
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskBegin();
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        listener.onTaskComplete(movie);
    }

    @Override
    protected Movie doInBackground(Movie... movies) {
        URL jsonRequestUrl = MovieDbUtils.buildMovieUrl(movieID);

        try {
            //get JSON from URL
            String jsonResponse = MovieDbUtils.getJSONFromUrl(jsonRequestUrl);
            //parse data from JSON
            Movie movie = MovieDbUtils.parseMovieDetailJSON(jsonResponse);

            return movie;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
