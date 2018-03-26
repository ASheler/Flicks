package com.glaserproject.flicks.Favorites;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Content provider class for Favorites
 */

public class FavoritesContentProvider extends ContentProvider {

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    //setup DbHelper
    private FavoritesDbHelper mFavoritesDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        //add URIs to check with matcher
        //add Favorites directory
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES, FAVORITES);
        //add row of Favorites items
        uriMatcher.addURI(FavoritesContract.AUTHORITY, FavoritesContract.PATH_FAVORITES + "/#", FAVORITES_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        //init DatabaseHelper
        mFavoritesDbHelper = new FavoritesDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        //connect to db
        final SQLiteDatabase dbHelper = mFavoritesDbHelper.getReadableDatabase();

        //setup Matcher with incoming uri
        int match = sUriMatcher.match(uri);

        Cursor returnCursor;

        switch (match) {
            case FAVORITES:
                //query Return Cursor for Favorites directory
                returnCursor = dbHelper.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case FAVORITES_WITH_ID:
                //query Return Cursor for rows
                //get incoming id
                String id = uri.getPathSegments().get(1);
                //setup selection
                String mSelection = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
                //setup selection Arguments
                String[] mSelectionArgs = new String[]{id};


                //query cursor for our selection
                returnCursor = dbHelper.query(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri " + uri);
        }

        //notify Cursor Loader
        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        //connect to Db
        final SQLiteDatabase dbHelper = mFavoritesDbHelper.getWritableDatabase();
        //setup Matcher with incoming uri
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case FAVORITES:
                //insert into db
                long id = dbHelper.insert(FavoritesContract.FavoritesEntry.TABLE_NAME, null, values);

                //check insertion status
                if (id > 0) {
                    //successful
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI, id);
                } else {
                    //error inserting
                    throw new android.database.SQLException("Failed to insert row " + values);
                }

                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);

        }
        //notify loader
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        //connect to db
        final SQLiteDatabase dbHelper = mFavoritesDbHelper.getWritableDatabase();
        //setup Matcher with incoming uri
        int match = sUriMatcher.match(uri);

        //get id from uri
        String id = uri.getPathSegments().get(1);
        //setup Where clause
        String whereClause = FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?";
        //setup whereArgs
        String[] whereArgs = new String[]{id};

        int favsDeleted;

        switch (match) {
            case FAVORITES_WITH_ID:
                //delete row in db
                favsDeleted = dbHelper.delete(FavoritesContract.FavoritesEntry.TABLE_NAME,
                        whereClause,
                        whereArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        if (favsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return favsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
