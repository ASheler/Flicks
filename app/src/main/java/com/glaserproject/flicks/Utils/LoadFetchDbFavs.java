package com.glaserproject.flicks.Utils;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import com.glaserproject.flicks.Favorites.FavoritesContract;
import com.glaserproject.flicks.Favorites.PassFavsToMovie;
import com.glaserproject.flicks.MyObjects.Movie;

/**
 * Class for fetching Favorites from Db
 */

public class LoadFetchDbFavs extends AsyncTask<Movie[], Void, Movie[]> {


    private Context context;

    //interface Listener
    public interface AsyncTaskCompleteListener<T> {
        void onTaskBegin();
        void onTaskComplete(T movies);
    }

    private AsyncTaskCompleteListener<Movie[]> listener;

    //initialize AsyncTask
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

        //setup Cursor
        Cursor cursor;

        try {
            //get Cursor from contentProvider
            cursor = context.getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    FavoritesContract.FavoritesEntry._ID);
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
        //parse cursor to Movie[]
        Movie[] movie = PassFavsToMovie.passFromCursor(cursor);
        //return Movie[]
        return movie;
    }

    @Override
    protected void onPostExecute(Movie[] movies) {
        super.onPostExecute(movies);
        listener.onTaskComplete(movies);
    }
}
