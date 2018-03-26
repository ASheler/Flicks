package com.glaserproject.flicks.Utils;

import android.os.AsyncTask;

import com.glaserproject.flicks.MyObjects.Movie;
import com.glaserproject.flicks.MyObjects.Review;
import com.glaserproject.flicks.MyObjects.Trailer;

import java.net.URL;

/**
 * Fetch and load JSOM wit Movie details
 */

public class LoadFetchJSONmovieDetail extends AsyncTask<Movie, Void, Movie> {

    private int movieID;
    private AsyncTaskCompleteListener<Movie> listener;
    public LoadFetchJSONmovieDetail(int movieID, AsyncTaskCompleteListener<Movie> listener) {
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
        URL jsonMovieDetailUrl = MovieDbUtils.buildMovieUrl(movieID);
        URL jsonTrailersUrl = MovieDbUtils.buildTrailersUrl(movieID);
        URL jsonReviewsUrl = MovieDbUtils.buildReviewsUrl(movieID);

        try {
            //get JSON from URL
            String jsonDetailsResponse = MovieDbUtils.getJSONFromUrl(jsonMovieDetailUrl);
            String jsonTrailersResponse = MovieDbUtils.getJSONFromUrl(jsonTrailersUrl);
            String jsonReviewsResponse = MovieDbUtils.getJSONFromUrl(jsonReviewsUrl);
            //parse data from JSON
            Movie movie = MovieDbUtils.parseMovieDetailJSON(jsonDetailsResponse);

            Trailer[] trailer = MovieDbUtils.parseTrailersJSON(jsonTrailersResponse);
            Review[] review = MovieDbUtils.parseReviewsJSON(jsonReviewsResponse);

            movie.putTrailer(trailer);
            movie.putReview(review);


            return movie;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public interface AsyncTaskCompleteListener<T> {
        void onTaskBegin();

        void onTaskComplete(T movie);
    }
}
