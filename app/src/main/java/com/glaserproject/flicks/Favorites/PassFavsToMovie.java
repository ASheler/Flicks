package com.glaserproject.flicks.Favorites;

import android.database.Cursor;

import com.glaserproject.flicks.MyObjects.Movie;

/**
 * Class for passing cursor to Movie[] object
 */

public class PassFavsToMovie {

    public static Movie[] passFromCursor (Cursor cursor){
        //setup Movie[] object
        int count = cursor.getCount();
        Movie[] mMovie = new Movie[count];

        //fill the object
        if (cursor.moveToNext()){
            do{
                String title = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE));
                int id = cursor.getInt(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID));
                String posterPath = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH));
                String releaseDate = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE));
                String backdropPath = cursor.getString(cursor.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH));
                Movie tempMovie = new Movie(id, title, posterPath, releaseDate, backdropPath);
                mMovie[cursor.getPosition()] = tempMovie;
            }while ((cursor.moveToNext()));
        }
        return mMovie;
    }
}
