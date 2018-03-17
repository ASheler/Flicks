package com.glaserproject.flicks.Utils;

import android.net.Uri;

import com.glaserproject.flicks.Movie.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Internet & JSON utils
 */

public class MovieDbUtils {

    //JSON variables
    public static final String JSON_RESULTS_KEY = "results";
    public static final String JSON_MOVIEID_KEY = "id";
    public static final String JSON_VOTECOUNT_KEY = "vote_count";
    public static final String JSON_VOTEAVERAGE_KEY = "vote_average";
    public static final String JSON_MOVIETITLE_KEY = "title";
    public static final String JSON_POPULARITY_KEY = "popularity";
    public static final String JSON_POSTERPATH_KEY = "poster_path";
    public static final String JSON_ORIGLANG_KEY = "original_language";
    public static final String JSON_ORIGTITLE_KEY = "original_title";
    public static final String JSON_GENRES_KEY = "genre_ids";
    public static final String JSON_BACKDROPPATH_KEY = "backdrop_path";
    public static final String JSON_ADULT_KEY = "adult";
    public static final String JSON_OVERVIEW_KEY = "overview";
    public static final String JSON_RELEASEDATE_KEY = "release_date";
    public static final String JSON_BUDGET_KEY = "budget";
    public static final String JSON_TAGLINE_KEY = "tagline";
    public static final String JSON_REVENUE_KEY = "revenue";

    //URL variables
    private final static String MOVIE_DB_API_KEY = "put_your_api_here";  // MovieDB API Key removed for privacy issues. Get you
    private final static String MOVIE_DB_API_KEY_STRING = "api_key";
    private final static String MOVIE_DB_URL_BASE = "http://api.themoviedb.org/3/";
    private final static String MOVIE_DB_PATH_MOVIE = "movie";
    private final static String MOVIE_DB_PATH_POPULAR = "popular";
    private final static String MOVIE_DB_PATH_TOP_RATED = "top_rated";
    private final static String MOVIE_DB_PATH_NOW_PLAYING = "now_playing";
    private final static String MOVIE_DB_PATH_UPCOMING = "upcoming";

    public static URL buildUrl(int streamSelection) {
        String currentEndPoint;

        //get different path for different sort selection
        switch (streamSelection) {
            case 0:
                currentEndPoint = MOVIE_DB_PATH_POPULAR;
                break;
            case 1:
                currentEndPoint = MOVIE_DB_PATH_TOP_RATED;
                break;
            case 2:
                currentEndPoint = MOVIE_DB_PATH_NOW_PLAYING;
                break;
            case 3:
                currentEndPoint = MOVIE_DB_PATH_UPCOMING;
                break;
            default:
                currentEndPoint = MOVIE_DB_PATH_POPULAR;

        }

        //build URI basic
        Uri builtUri = Uri.parse(MOVIE_DB_URL_BASE).buildUpon()
                .appendPath(MOVIE_DB_PATH_MOVIE)
                .appendPath(currentEndPoint)
                .appendQueryParameter(MOVIE_DB_API_KEY_STRING, MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //return built url
        return url;
    }

    //build URL for Movie Detail
    public static URL buildMovieUrl(int movieID) {
        Uri builtUri = Uri.parse(MOVIE_DB_URL_BASE).buildUpon()
                .appendPath(MOVIE_DB_PATH_MOVIE)
                .appendPath(Integer.toString(movieID))
                .appendQueryParameter(MOVIE_DB_API_KEY_STRING, MOVIE_DB_API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    //get stream from URL
    public static String getJSONFromUrl(URL url) throws IOException {

        //open url connection
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream inputStream = urlConnection.getInputStream();
            //setup scanner
            Scanner scanner = new Scanner(inputStream);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                //return scanned item
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            //close URL connection
            urlConnection.disconnect();
        }

    }

    //Get JSON Movie Detail into Simple Strings
    public static Movie parseMovieDetailJSON(String json) throws JSONException {
        String tagline;
        int revenue;
        String overview;
        int budget;
        String popularity;
        String voteAverage;

        JSONObject jsonRoot = new JSONObject(json);
        tagline = jsonRoot.optString(JSON_TAGLINE_KEY, "NOT FOUND");
        revenue = jsonRoot.optInt(JSON_REVENUE_KEY, 0);
        overview = jsonRoot.optString(JSON_OVERVIEW_KEY, "NOT FOUND");
        budget = jsonRoot.optInt(JSON_BUDGET_KEY, 0);
        popularity = jsonRoot.optString(JSON_POPULARITY_KEY, "NOT FOUND");
        voteAverage = jsonRoot.optString(JSON_VOTEAVERAGE_KEY, "NOT FOUND");

        //create movie to be send to Detail Activity
        Movie movie = new Movie(tagline, revenue, overview, budget, popularity, voteAverage);

        return movie;
    }


    //Get JSON All Movies into Simple Strings
    public static Movie[] parseMovieJSON(String json) throws JSONException {
        int id;
        int voteCount;
        String voteAverage;
        String movieTitle;
        String popularity;
        String posterPath;
        String origLang;
        String origTitle;
        List<Integer> genres;
        String backdropPath;
        boolean adult;
        String overview;
        String releaseDate;

        //get JSONObject - rood
        JSONObject jsonRoot = new JSONObject(json);
        JSONArray resultsRoot = jsonRoot.optJSONArray(JSON_RESULTS_KEY);

        //initialize Movie[] variable
        Movie[] movie = new Movie[resultsRoot.length()];

        //continually load into Movie[] var
        for (int i = 0; i < resultsRoot.length(); i++) {

            //create tempMovie for temporarily storing Movie vars
            Movie tempMovie;

            JSONObject results = resultsRoot.getJSONObject(i);

            id = results.optInt(JSON_MOVIEID_KEY, 0);
            voteCount = results.optInt(JSON_VOTECOUNT_KEY, 0);
            voteAverage = results.optString(JSON_VOTEAVERAGE_KEY, "NOT FOUND");
            movieTitle = results.optString(JSON_MOVIETITLE_KEY, "NOT FOUND");
            popularity = results.optString(JSON_POPULARITY_KEY, "NOT FOUND");
            posterPath = results.optString(JSON_POSTERPATH_KEY, "NOT FOUND");
            origLang = results.optString(JSON_ORIGLANG_KEY, "NOT FOUND");
            origTitle = results.optString(JSON_ORIGTITLE_KEY, "NOT FOUND");

            JSONArray genresArray = results.optJSONArray(JSON_GENRES_KEY);
            genres = new ArrayList<>();
            for (int x = 0; x < genresArray.length(); x++) {
                int val = genresArray.optInt(x);
                genres.add(val);
            }

            backdropPath = results.optString(JSON_BACKDROPPATH_KEY, "NOT FOUND");
            adult = results.optBoolean(JSON_ADULT_KEY, false);
            overview = results.optString(JSON_OVERVIEW_KEY, "NOT FOUND");
            releaseDate = results.optString(JSON_RELEASEDATE_KEY, "NOT FOUND");

            //put data into Movie var
            tempMovie = new Movie(
                    id,
                    voteCount,
                    voteAverage,
                    movieTitle,
                    popularity,
                    posterPath,
                    origLang,
                    origTitle,
                    genres,
                    backdropPath,
                    adult,
                    overview,
                    releaseDate

            );
            movie[i] = tempMovie;
        }


        return movie;
    }


}
