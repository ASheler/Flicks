package com.glaserproject.flicks.Favorites;

import android.database.Cursor;

import com.glaserproject.flicks.MyObjects.Movie;

/**
 * Created by ondra on 3/24/2018.
 */

public class PassFavsToMovie {

    public static Movie[] passFromCursor (Cursor cursor){
        int count = cursor.getCount();
        Movie[] mMovie = new Movie[count];

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


/*

        int position1 = cursor.getPosition();
        while (cursor.moveToNext()){
            int position = cursor.getPosition()+1;
            //mMovie[position].setMovieTitle("prdel");
            mMovie[position].setId(338970);
            mMovie[position].setPosterPath("dnxU4TaXKuFTv9kv6XEGyWEaD7v");
            mMovie[position].setReleaseDate("hovnooooo");
        }
*/

        return mMovie;
    }
}
