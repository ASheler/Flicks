package com.glaserproject.flicks;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.glaserproject.flicks.MyObjects.Movie;
import com.glaserproject.flicks.Utils.BaseViewModel;
import com.glaserproject.flicks.Utils.ConstantsClass;
import com.glaserproject.flicks.Utils.LoadFetchDbFavs;
import com.glaserproject.flicks.Utils.LoadFetchJSONmovies;
import com.glaserproject.flicks.RvAdapter.TileAdapter;

public class MainActivity extends AppCompatActivity implements
        TileAdapter.TileAdapterOnClickHandler {

    ProgressBar loadingIndicatorPB;

    Button reloadButton;
    TextView noFavsSavedTV;

    TileAdapter mTileAdapter;
    RecyclerView mMoviesRV;
    int currentSelection;
    Movie[] movies;
    private BaseViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //set Toolbar
        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);


        //Find Views in layout
        loadingIndicatorPB = findViewById(R.id.loading_indicator_pb);
        mMoviesRV = findViewById(R.id.movies_rv);
        reloadButton = findViewById(R.id.reloadButton);
        noFavsSavedTV = findViewById(R.id.no_favs_saved_TV);

        //initialize viewModel
        viewModel = ViewModelProviders.of(this).get(BaseViewModel.class);

        //check and setup viewModel
        if (viewModel.isFilled()) {
            currentSelection = viewModel.getCurrentSelection();
            movies = viewModel.getMovie();
        } else {
            currentSelection = 0;
        }

        //setup RecyclerView & Layout Manager
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);

        //set AutoMeasure Off to fully load on screen
        //layoutManager.setAutoMeasureEnabled(false);
        mMoviesRV.setLayoutManager(layoutManager);
        mMoviesRV.setHasFixedSize(true);
        //Initialize empty TileAdapter for better performance
        mTileAdapter = new TileAdapter(this);
        //set Adapter for RecyclerView
        mMoviesRV.setAdapter(mTileAdapter);


        //setup Reload Button
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNetworkAvailable(getApplicationContext())) {
                    recreate();
                    reloadButton.setVisibility(View.GONE);
                } else {
                    Toast.makeText(MainActivity.this, R.string.no_connection_toast, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    @Override
    public void onClick(Movie movie) {
        //onClick leads to Detail Activity
        Intent intent = new Intent(this, DetailActivity.class);
        //send movie object
        intent.putExtra(ConstantsClass.SELECTED_MOVIE_EXTRA_KEY, movie);
        //start Detail Activity
        startActivityForResult(intent, 1);
    }

    //update UI with new data
    public void updateUI(Movie[] movies) {

        if (movies.length == 0) {
            noFavsSavedTV.setVisibility(View.VISIBLE);
        } else {
            noFavsSavedTV.setVisibility(View.GONE);
        }

        mTileAdapter.setMovieData(movies);
    }

    //Check if Network connection is available
    public boolean isNetworkAvailable(Context context) {
        //set connectivity manager
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        //check if ActiveNetwork isn't null && is Connected
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        //spinner
        MenuItem item = menu.findItem(R.id.spinner_menu);
        Spinner spinner = (Spinner) item.getActionView();

        //adapter for spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.movie_spinner_choices, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setSelection(currentSelection);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSelection = position;

                //check if we have data loaded in ViewModel
                //if we have data - load it
                if (viewModel.isFilled() && position == viewModel.getCurrentSelection()) {
                    updateUI(viewModel.getMovie());

                    //if not - fetch it
                } else {                //If connection available - load!
                    if (isNetworkAvailable(MainActivity.this)) {
                        //hide Reload Button
                        reloadButton.setVisibility(View.GONE);
                        //connected - load data
                        //check if favorites
                        if (currentSelection != 4) {
                            //load from JSON
                            new LoadFetchJSONmovies(position, new LoadFetchJSONCompleteListener()).execute();
                        } else {
                            //load favorites from db
                            new LoadFetchDbFavs(getApplicationContext(), new LoadFetchDbFavsCompleteListener()).execute();
                        }
                    } else {
                        //hide Reload Button
                        reloadButton.setVisibility(View.VISIBLE);
                        //set error message on no connection
                        Toast.makeText(MainActivity.this, R.string.no_connection_toast, Toast.LENGTH_SHORT).show();
                        //clear ui and set null data to rv
                        mTileAdapter.setNullData();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //nothing selected - do nothing
            }
        });


        return true;
    }

    //check if favorites changed -> if so, reload the list
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK && currentSelection == 4 && data.getBooleanExtra("hovno", false)) {
                new LoadFetchDbFavs(getApplicationContext(), new LoadFetchDbFavsCompleteListener()).execute();
            }
        }
    }

    //listener for JSON fetcher class
    public class LoadFetchJSONCompleteListener implements LoadFetchJSONmovies.AsyncTaskCompleteListener<Movie[]> {

        @Override
        public void onTaskBegin() {
            //show loading button
            loadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Movie[] movies) {

            //initialize viewModel with current movies and selection
            viewModel.init(movies, currentSelection);

            //hide loading button
            loadingIndicatorPB.setVisibility(View.INVISIBLE);
            //Update UI
            updateUI(movies);


        }
    }

    //Favorites Complete Listener
    public class LoadFetchDbFavsCompleteListener implements LoadFetchDbFavs.AsyncTaskCompleteListener<Movie[]> {

        @Override
        public void onTaskBegin() {
            //show loading button
            loadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Movie[] movies) {

            //init viewModel with current selection and Movies
            viewModel.init(movies, currentSelection);

            //hide loading button
            loadingIndicatorPB.setVisibility(View.INVISIBLE);

            //update UI with loaded Fav movies
            updateUI(movies);
        }
    }
}