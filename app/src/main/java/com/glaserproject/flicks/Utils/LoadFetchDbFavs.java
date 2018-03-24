package com.glaserproject.flicks.Utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.glaserproject.flicks.Favorites.FavoritesContract;
import com.glaserproject.flicks.Favorites.PassFavsToMovie;
import com.glaserproject.flicks.MyObjects.Movie;

/**
 * Created by ondra on 3/24/2018.
 */

public class LoadFetchDbFavs extends AsyncTask<Movie[], Void, Movie[]> {


    Context context;

    public interface AsyncTaskCompleteListener<T> {
        void onTaskBegin();
        void onTaskComplete(T movies);

    }

    private AsyncTaskCompleteListener<Movie[]> listener;

    public LoadFetchDbFavs(Context context, AsyncTaskCompleteListener<Movie[]> listener){
        this.listener = listener;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onTaskBegin();
    }

    @Override
    protected Movie[] doInBackground(Movie[]... movies) {
        Cursor cursor;
        try {
            cursor = context.getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoritesContract.FavoritesEntry._ID);
        } catch (Exception e){
            Log.e("HOVNOOOO", "FAILED TO LOAD DATA");
            e.printStackTrace();
            return null;
        }
        Movie[] movie = PassFavsToMovie.passFromCursor(cursor);
        return movie;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }
}
