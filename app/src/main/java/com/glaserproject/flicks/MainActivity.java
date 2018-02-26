package com.glaserproject.flicks;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.glaserproject.flicks.Movie.Movie;
import com.glaserproject.flicks.NetUtils.MovieDbUtils;
import com.glaserproject.flicks.RvAdapter.TileAdapter;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
        //implements TileAdapter.TileAdapterClickHandler, LoaderManager.LoaderCallbacks<String[]>{

    TextView textView;
    private ProgressBar mLoadingIndicator;

    TileAdapter mTileAdapter;
    private RecyclerView mMoviesRV;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingIndicator = findViewById(R.id.loading_indicator_pb);

        mLoadingIndicator.setVisibility(View.VISIBLE);

        mMoviesRV = findViewById(R.id.movies_rv);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mMoviesRV.setLayoutManager(layoutManager);
        mMoviesRV.setHasFixedSize(true);
        mTileAdapter = new TileAdapter();
        mMoviesRV.setAdapter(mTileAdapter);


        if (isNetworkAvailable(this)) {
            new loadJSON().execute();
        } else {
            textView.setText("NO CONNECTION");
        }

    }



    public class loadJSON extends AsyncTask<Movie[], Void, Movie[]>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Movie[] doInBackground(Movie[]... movies) {

            URL weatherRequestUrl = MovieDbUtils.buildUrl();

            try {
                String jsonWeatherResponse = MovieDbUtils.getJSONFromUrl(weatherRequestUrl);
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
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            updateUI(movies);

        }
    }


    public void updateUI(Movie[] movies){
        mTileAdapter.setMovieData(movies);
        mTileAdapter.notifyDataSetChanged();
    }


    //Check if Network connection is available
    public boolean isNetworkAvailable(Context context) {
        //set connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //check if ActiveNetwork isn't null && is Connected
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
