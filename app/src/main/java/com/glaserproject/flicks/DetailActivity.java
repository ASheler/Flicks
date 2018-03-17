package com.glaserproject.flicks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
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
import java.text.NumberFormat;

public class DetailActivity extends AppCompatActivity {

    int movieID;
    TextView taglineTV, overviewTV, budgetTV, revenueTV, popularityTV, releaseDateTV, voteAverageTV;
    TextView overviewLabelTV, budgetLabelTV, revenueLabelTV, popularityLabelTV, releaseDateLabelTV, voteAverageLabelTV;
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
        overviewLabelTV = findViewById(R.id.overview_label_tv);
        budgetLabelTV = findViewById(R.id.budget_label_tv);
        revenueLabelTV = findViewById(R.id.revenue_label_tv);
        popularityLabelTV = findViewById(R.id.popularity_label_tv);
        releaseDateLabelTV = findViewById(R.id.releaseDate_label_tv);
        voteAverageLabelTV = findViewById(R.id.voteAverage_label_tv);


        if (isNetworkAvailable(this)) {
            new loadDataFromJSON().execute();
        } else {
            //Set no connection message as Tagline below image
            taglineTV.setVisibility(View.VISIBLE);
            taglineTV.setText(R.string.no_connection_message);
        }

    }

    public void updateUI(Movie movie) {
        //first make UI elements visible
        showUIelements();

        //get and format integers
        String budgetString = NumberFormat.getCurrencyInstance().format(movie.getBudget());
        String revenueString = NumberFormat.getCurrencyInstance().format(movie.getRevenue());

        //update TVs
        taglineTV.setText(movie.getTagline());
        overviewTV.setText(movie.getOverview());
        budgetTV.setText(budgetString);
        revenueTV.setText(revenueString);
        popularityTV.setText(movie.getPopularity());
        releaseDateTV.setText(releaseDate);
        voteAverageTV.setText(movie.getVoteAverage());


    }

    //set all UI elements visible
    public void showUIelements() {
        overviewLabelTV.setVisibility(View.VISIBLE);
        budgetLabelTV.setVisibility(View.VISIBLE);
        revenueLabelTV.setVisibility(View.VISIBLE);
        popularityLabelTV.setVisibility(View.VISIBLE);
        releaseDateLabelTV.setVisibility(View.VISIBLE);
        voteAverageLabelTV.setVisibility(View.VISIBLE);

    }

    //Check if Network connection is available
    public boolean isNetworkAvailable(Context context) {
        //set connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //check if ActiveNetwork isn't null && is Connected
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
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

}
