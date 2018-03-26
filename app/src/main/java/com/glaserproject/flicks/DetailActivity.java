package com.glaserproject.flicks;

import android.arch.lifecycle.ViewModelProviders;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.glaserproject.flicks.Favorites.FavoritesContract;
import com.glaserproject.flicks.MyObjects.Movie;
import com.glaserproject.flicks.RvAdapter.ReviewsAdapeter;
import com.glaserproject.flicks.RvAdapter.TileAdapter;
import com.glaserproject.flicks.RvAdapter.TrailersAdapter;
import com.glaserproject.flicks.Utils.BaseViewModel;
import com.glaserproject.flicks.Utils.ConstantsClass;
import com.glaserproject.flicks.Utils.LoadFetchJSONmovieDetail;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.text.NumberFormat;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailersAdapterOnClickHandler {

    int movieID;
    //Bind Views with ButterKnife
    @BindView(R.id.tagline_tv)
    TextView taglineTV;
    @BindView(R.id.overview_tv)
    TextView overviewTV;
    @BindView(R.id.overview_label_tv)
    TextView overviewLabelTV;
    @BindView(R.id.budget_tv)
    TextView budgetTV;
    @BindView(R.id.budget_label_tv)
    TextView budgetLabelTV;
    @BindView(R.id.revenue_tv)
    TextView revenueTV;
    @BindView(R.id.revenue_label_tv)
    TextView revenueLabelTV;
    @BindView(R.id.popularity_tv)
    TextView popularityTV;
    @BindView(R.id.popularity_label_tv)
    TextView popularityLabelTV;
    @BindView(R.id.releaseDate_tv)
    TextView releaseDateTV;
    @BindView(R.id.releaseDate_label_tv)
    TextView releaseDateLabelTV;
    @BindView(R.id.voteAverage_tv)
    TextView voteAverageTV;
    @BindView(R.id.voteAverage_label_tv)
    TextView voteAverageLabelTV;
    @BindView(R.id.loading_indicator_pb)
    ProgressBar loadingIndicatorPB;
    @BindView(R.id.trailers_rv)
    RecyclerView trailersRV;
    @BindView(R.id.reviews_rv)
    RecyclerView reviewsRV;
    @BindView(R.id.favs_star_iv)
    ImageView favsStarIV;
    @BindView(R.id.line)
    View lineView;
    @BindView(R.id.line2)
    View line2View;
    @BindView(R.id.trailers_label_tv)
    TextView trailersLabelTV;
    @BindView(R.id.reviews_label_tv)
    TextView reviewsLabelTV;


    String releaseDate;
    TrailersAdapter mTrailersAdapter;
    ReviewsAdapeter mReviewsAdapter;

    String movieTitle;
    String backdropPath;
    String posterPath;
    boolean isInFavs;
    Movie movie;
    boolean favsChanged;
    private BaseViewModel viewModel;

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

        //init viewModel
        viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        //check and setup viewModel
        if (viewModel.getSingleMovie() != null) {
            movie = viewModel.getSingleMovie();
        } else {
            //get Movie object from Intent
            movie = getIntent().getParcelableExtra(ConstantsClass.SELECTED_MOVIE_EXTRA_KEY);
            viewModel.initSingleMovie(movie);
        }


        //get MovieID, posterPath and Backdrop
        movieID = movie.getId();
        backdropPath = movie.getBackdropPath();
        posterPath = movie.getPosterPath();

        //set Title
        movieTitle = movie.getMovieTitle();
        getSupportActionBar().setTitle(movieTitle);

        //get Release Date
        releaseDate = movie.getReleaseDate();

        //check if movie is in Favorites
        checkIfInFavs();


        //Load Image to Main Picture
        ImageView imageView = findViewById(R.id.imageView);
        Picasso.with(this).load(ConstantsClass.URL_PICTURE_BASE_W500 + backdropPath).into(imageView);


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


        //check if we have stored UI data
        if (viewModel.isFilled()) {
            //if data -> show them
            updateUI(viewModel.getMovieDetail());
        } else {
            if (isNetworkAvailable(this)) {
                //connected - fetch data
                new LoadFetchJSONmovieDetail(movieID, new LoadFetchJSONCompleteListener()).execute();

            } else {
                //Set no connection message as Tagline below image
                taglineTV.setVisibility(View.VISIBLE);
                taglineTV.setText(R.string.no_connection_message);
            }
        }

        //setup Favorite Star OnClickListener
        favsStarIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favsStarClick();
            }
        });


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

        //Change Favorites Star accordingly
        changeFavsStar();


    }

    //set all UI elements visible
    public void showUIelements() {
        overviewLabelTV.setVisibility(View.VISIBLE);
        budgetLabelTV.setVisibility(View.VISIBLE);
        revenueLabelTV.setVisibility(View.VISIBLE);
        popularityLabelTV.setVisibility(View.VISIBLE);
        releaseDateLabelTV.setVisibility(View.VISIBLE);
        voteAverageLabelTV.setVisibility(View.VISIBLE);
        lineView.setVisibility(View.VISIBLE);
        line2View.setVisibility(View.VISIBLE);
        trailersLabelTV.setVisibility(View.VISIBLE);
        reviewsLabelTV.setVisibility(View.VISIBLE);

    }

    //Check if Network connection is available
    public boolean isNetworkAvailable(Context context) {
        //set connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //check if ActiveNetwork isn't null && is Connected
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public void onTrailerClick(String videoKey) {
        //run intent on trailer icon click
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ConstantsClass.URL_YOUTUBUE_VIDEO_BASE).buildUpon()
                .appendQueryParameter(ConstantsClass.URL_YOUTUBE_VIEW_KEY_STRING, videoKey)
                .build());
        startActivity(intent);
    }

    @Override
    public void onTrailerLongClick(String videoName, String videoKey) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("youtu.be")
                .appendPath(videoKey);
        String myUrl = videoName + " - " + builder.build().toString();

        ShareCompat.IntentBuilder
                .from(this)
                .setType("text/plain")
                .setChooserTitle(R.string.trailer_share_dialog_title)
                .setText(myUrl)
                .startChooser();
    }

    //change star accordingly to the state
    public void changeFavsStar() {
        //if movie is in favorites
        if (isInFavs) {
            //set gold star
            favsStarIV.setImageResource(R.drawable.ic_favs_star_gold);
        } else {
            //if not -> set gray star
            favsStarIV.setImageResource(R.drawable.ic_favs_star_grey);
        }


    }

    //check if movie is in favorites
    public void checkIfInFavs() {
        //get cursor from Content Provider
        Cursor cursor = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI, null, FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + "=?", new String[]{Integer.toString(movieID)}, null);
        int length = cursor.getCount();
        //check length of cursor
        //if length > 0 -> movie is in favorites
        if (length > 0) {
            //movie is in favorites
            isInFavs = true;
        } else {
            //movie is not in favorites
            isInFavs = false;
        }
        //change star accordingly
        changeFavsStar();
    }

    //click action on star
    public void favsStarClick() {
        //first check if is in favs already
        if (isInFavs) {
            //movie is in favorites -> use ContentProvider to delete it

            //setup URI with movie ID
            Uri uri = FavoritesContract.FavoritesEntry.CONTENT_URI;
            uri = uri.buildUpon().appendPath(Integer.toString(movieID)).build();

            //get Content Resolver and delete rows
            getContentResolver().delete(uri, null, null);

            //notify user
            Toast.makeText(this, R.string.removed_from_favs_text, Toast.LENGTH_SHORT).show();

            //change state of star
            isInFavs = false;
            changeFavsStar();
        } else {
            //movie is not in favorites currently

            //setup Content Values
            ContentValues contentValues = new ContentValues();
            //put info into contentValues
            contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID, movieID);
            contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_MOVIE_TITLE, movieTitle);
            contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, posterPath);
            contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH, backdropPath);
            contentValues.put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE, releaseDate);

            //put contentValues to db through Content Provider
            Uri uri = getContentResolver().insert(FavoritesContract.FavoritesEntry.CONTENT_URI, contentValues);

            //check if insertion was successful
            if (uri != null) {
                //change state of star
                isInFavs = true;
                changeFavsStar();

                //notify user
                Toast.makeText(this, R.string.added_to_favs_text, Toast.LENGTH_SHORT).show();
            }
        }

        favsChanged = true;

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("hovno", favsChanged);
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    //listener for JSON fetcher class
    public class LoadFetchJSONCompleteListener implements LoadFetchJSONmovieDetail.AsyncTaskCompleteListener<Movie> {

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
            //init Movie Detail viewModel
            viewModel.initMovieDetail(movie);

        }
    }

}
