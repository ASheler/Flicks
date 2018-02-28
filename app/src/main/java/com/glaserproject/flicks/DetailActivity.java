package com.glaserproject.flicks;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.glaserproject.flicks.Movie.Movie;
import com.glaserproject.flicks.Utils.ConstantsClass;
import com.glaserproject.flicks.Utils.MovieDbUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    int movieID;
    TextView taglineTV, overviewTV, budgetTV, revenueTV, popularityTV, releaseDateTV, voteAverageTV;
    String releaseDate;
    ProgressBar loadingIndicatorPB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //setup toolbar as SupportActionBar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //get intent that lead to this Activity
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }
        //get MovieID and Backdrop from intent
        movieID = intent.getExtras().getInt(ConstantsClass.MOVIE_ID_INTENT_EXTRA_KEY, 0);
        String backdropPath = intent.getExtras().getString(ConstantsClass.BACKDROP_PATH_INTENT_EXTRA_KEY);

        //set Title
        String movieTitle = intent.getExtras().getString(ConstantsClass.TITLE_INTENT_EXTRA_KEY);
        getSupportActionBar().setTitle(movieTitle);

        //get Release Date
        releaseDate = intent.getExtras().getString(ConstantsClass.RELEASE_DATE_INTENT_EXTRA_KEY, "NOT FOUND");

        //Load Image to Main Picture
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.with(this).load(ConstantsClass.URL_PICTURE_BASE_W500 + backdropPath).into(imageView);

        //initialize UI
        taglineTV = findViewById(R.id.tagline_tv);
        overviewTV = findViewById(R.id.overview_tv);
        budgetTV = findViewById(R.id.budget_tv);
        revenueTV = findViewById(R.id.revenue_tv);
        popularityTV = findViewById(R.id.popularity_tv);
        releaseDateTV = findViewById(R.id.releaseDate_tv);
        voteAverageTV = findViewById(R.id.voteAverage_tv);
        loadingIndicatorPB = findViewById(R.id.loading_indicator_pb);

        //TODO: Check internet connection
        new loadDataFromJSON().execute();


    }


    public class loadDataFromJSON extends AsyncTask<Movie, Void, Movie> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Show loading indicator
            loadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        protected Movie doInBackground(Movie... movies) {
            //build URL for JSON parsing
            URL weatherRequestUrl = MovieDbUtils.buildMovieUrl(movieID);

            try {
                //get JSON from URL
                String jsonWeatherResponse = MovieDbUtils.getJSONFromUrl(weatherRequestUrl);
                //parse data from JSON
                Movie movie = MovieDbUtils.parseMovieDetailJSON(jsonWeatherResponse);

                return movie;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Movie movie) {
            super.onPostExecute(movie);
            //set Loading Icon INVISIBLE
            loadingIndicatorPB.setVisibility(View.INVISIBLE);
            //update UI
            updateUI(movie);
        }
    }

    public void updateUI(Movie movie) {

        taglineTV.setText(movie.getTagline());
        overviewTV.setText(movie.getOverview());
        budgetTV.setText(Integer.toString(movie.getBudget()));
        revenueTV.setText(Integer.toString(movie.getRevenue()));
        popularityTV.setText(movie.getPopularity());
        releaseDateTV.setText(releaseDate);

        //TODO: For some reason doesn't show average in some movies (COCO)
        voteAverageTV.setText(movie.getVoteAverage());

    }

}
