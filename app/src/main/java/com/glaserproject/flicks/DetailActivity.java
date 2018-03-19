package com.glaserproject.flicks;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glaserproject.flicks.MyObjects.Movie;
import com.glaserproject.flicks.MyObjects.Review;
import com.glaserproject.flicks.MyObjects.Trailer;
import com.glaserproject.flicks.RvAdapter.ReviewsAdapeter;
import com.glaserproject.flicks.RvAdapter.TrailersAdapter;
import com.glaserproject.flicks.Utils.ConstantsClass;
import com.glaserproject.flicks.Utils.LoadFetchJSONmovieDetail;
import com.glaserproject.flicks.Utils.MovieDbUtils;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailersAdapterOnClickHandler{

    int movieID;
    //Bind Views with ButterKnife
    @BindView(R.id.tagline_tv) TextView taglineTV;
    @BindView(R.id.overview_tv) TextView overviewTV;
    @BindView(R.id.overview_label_tv) TextView overviewLabelTV;
    @BindView(R.id.budget_tv) TextView budgetTV;
    @BindView(R.id.budget_label_tv) TextView budgetLabelTV;
    @BindView(R.id.revenue_tv) TextView revenueTV;
    @BindView(R.id.revenue_label_tv) TextView revenueLabelTV;
    @BindView(R.id.popularity_tv) TextView popularityTV;
    @BindView(R.id.popularity_label_tv) TextView popularityLabelTV;
    @BindView(R.id.releaseDate_tv) TextView releaseDateTV;
    @BindView(R.id.releaseDate_label_tv) TextView releaseDateLabelTV;
    @BindView(R.id.voteAverage_tv) TextView voteAverageTV;
    @BindView(R.id.voteAverage_label_tv) TextView voteAverageLabelTV;
    @BindView(R.id.loading_indicator_pb) ProgressBar loadingIndicatorPB;
    @BindView(R.id.trailers_rv) RecyclerView trailersRV;
    @BindView(R.id.reviews_rv) RecyclerView reviewsRV;

    String releaseDate;
    TrailersAdapter mTrailersAdapter;
    ReviewsAdapeter mReviewsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //setup toolbar as SupportActionBar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        //init ButterKnife
        ButterKnife.bind(this);

        //get intent that lead to this Activity
        Intent intent = getIntent();
        if (intent == null) {
            finish();
        }

        //get Movie object from Intent
        Movie movie = getIntent().getParcelableExtra(ConstantsClass.SELECTED_MOVIE_EXTRA_KEY);

        //get MovieID and Backdrop
        movieID = movie.getId();
        String backdropPath = movie.getBackdropPath();

        //set Title
        String movieTitle = movie.getMovieTitle();
        getSupportActionBar().setTitle(movieTitle);

        //get Release Date
        releaseDate = movie.getReleaseDate();



        //Load Image to Main Picture
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.with(this).load(ConstantsClass.URL_PICTURE_BASE_W500 + backdropPath).into(imageView);


        if (isNetworkAvailable(this)) {
            //connected - fetch data
            new LoadFetchJSONmovieDetail(movieID, new LoadFetchJSONCompleteListener()).execute();

        } else {
            //Set no connection message as Tagline below image
            taglineTV.setVisibility(View.VISIBLE);
            taglineTV.setText(R.string.no_connection_message);
        }


        //setup RecyclerView & Layout Manager for Trailers
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        trailersRV.setLayoutManager(layoutManager);
        trailersRV.setHasFixedSize(true);

        //Initialize empty TileAdapter for better performance
        mTrailersAdapter = new TrailersAdapter(this);
        //set Adapter for RecyclerView
        trailersRV.setAdapter(mTrailersAdapter);


        ///setup RV & LM for Reviews
        LinearLayoutManager reviewsLayoutManager = new LinearLayoutManager(this);

        reviewsRV.setLayoutManager(reviewsLayoutManager);
        reviewsRV.setHasFixedSize(true);

        //Initialize empty TileAdapter for better performance
        mReviewsAdapter = new ReviewsAdapeter();
        //set Adapter for RecyclerView
        reviewsRV.setAdapter(mReviewsAdapter);





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

        //set trailers
        mTrailersAdapter.setTrailersData(movie.getTrailers());

        //set reviews
        mReviewsAdapter.setReviewsData(movie.getReviews());


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

    @Override
    public void onClick(String videoKey) {

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantsClass.URL_YOUTUBUE_VIDEO_BASE).buildUpon()
                .appendQueryParameter(ConstantsClass.URL_YOUTUBE_VIEW_KEY_STRING, videoKey)
                .build());
        startActivity(intent);
    }


    //listener for JSON fetcher class
    public class LoadFetchJSONCompleteListener implements LoadFetchJSONmovieDetail.AsyncTaskCompleteListener<Movie>{

        @Override
        public void onTaskBegin() {
            //show loading indicator
            loadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Movie movie) {
            //set Loading Icon INVISIBLE
            loadingIndicatorPB.setVisibility(View.INVISIBLE);
            //update UI
            updateUI(movie);
        }
    }

}
