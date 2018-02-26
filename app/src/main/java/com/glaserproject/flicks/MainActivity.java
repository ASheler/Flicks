package com.glaserproject.flicks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glaserproject.flicks.Movie.Movie;
import com.glaserproject.flicks.Utils.ConstantsClass;
import com.glaserproject.flicks.Utils.MovieDbUtils;
import com.glaserproject.flicks.RvAdapter.TileAdapter;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements
        TileAdapter.TileAdapterOnClickHandler {
        //implements TileAdapter.TileAdapterClickHandler, LoaderManager.LoaderCallbacks<String[]>{

    TextView textView;
    ProgressBar mLoadingIndicator;

    TileAdapter mTileAdapter;
    RecyclerView mMoviesRV;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Find Views in layout
        mLoadingIndicator = findViewById(R.id.loading_indicator_pb);
        mMoviesRV = findViewById(R.id.movies_rv);

        //setup RecyclerView & Layout Manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        //set AutoMeasure Off to fully load on screen
        layoutManager.setAutoMeasureEnabled(false);
        mMoviesRV.setLayoutManager(layoutManager);
        mMoviesRV.setHasFixedSize(true);
        //Initialize empty TileAdapter for better performance
        mTileAdapter = new TileAdapter(this);
        //set Adapter for RecyclerView
        mMoviesRV.setAdapter(mTileAdapter);

        //check Network availability
        if (isNetworkAvailable(this)) {
            //connected - load data
            new loadJSON().execute();
        } else {
            //set error message on no connection
            textView.setText("NO CONNECTION");
        }

    }


    @Override
    public void onClick(int movieId) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(ConstantsClass.MOVIE_ID_INTENT_EXTRA_KEY, movieId);
        startActivity(intent);
    }


    public class loadJSON extends AsyncTask<Movie[], Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //show loading Icon
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie[] doInBackground(Movie[]... movies) {

            //build URL for JSON parsing
            URL weatherRequestUrl = MovieDbUtils.buildUrl();

            try {
                //get JSON from URL
                String jsonWeatherResponse = MovieDbUtils.getJSONFromUrl(weatherRequestUrl);
                //parse data from JSON
                Movie[] movie = MovieDbUtils.parseMovieJSON(jsonWeatherResponse);

                return movie;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie[] movies) {
            super.onPostExecute(movies);
            //set Loading Icon INVISIBLE
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            //Update UI
            updateUI(movies);

        }
    }

    //update UI with new data
    public void updateUI(Movie[] movies){
        mTileAdapter.setMovieData(movies);
    }


    //Check if Network connection is available
    public boolean isNetworkAvailable(Context context) {
        //set connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //check if ActiveNetwork isn't null && is Connected
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}