package com.glaserproject.flicks.Favorites;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Contract class for FavoritesDbHelper
 */

public class FavoritesContract {


    public static final String AUTHORITY = "com.glaserproject.flicks";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVORITES = "favorites";



    public static final class FavoritesEntry implements BaseColumns {

        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITES).build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "movie_title";
        public static final String COLUMN_POSTER_PATH = "poster_path";

    }

}
