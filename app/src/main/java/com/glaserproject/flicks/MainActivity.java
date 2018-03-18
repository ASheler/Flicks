package com.glaserproject.flicks;

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
import android.widget.Toast;

import com.glaserproject.flicks.Movie.Movie;
import com.glaserproject.flicks.Utils.ConstantsClass;
import com.glaserproject.flicks.Utils.LoadFetchJSONmovies;
import com.glaserproject.flicks.RvAdapter.TileAdapter;

public class MainActivity extends AppCompatActivity implements
        TileAdapter.TileAdapterOnClickHandler {

    ProgressBar loadingIndicatorPB;

    Button reloadButton;

    TileAdapter mTileAdapter;
    RecyclerView mMoviesRV;
    int currentSelection;


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

        //initialize selection
        currentSelection = 0;

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
        intent.putExtra(ConstantsClass.MOVIE_ID_INTENT_EXTRA_KEY, movie.getId());
        intent.putExtra(ConstantsClass.BACKDROP_PATH_INTENT_EXTRA_KEY, movie.getBackdropPath());
        intent.putExtra(ConstantsClass.TITLE_INTENT_EXTRA_KEY, movie.getMovieTitle());
        intent.putExtra(ConstantsClass.RELEASE_DATE_INTENT_EXTRA_KEY, movie.getReleaseDate());

        startActivity(intent);
    }

    //update UI with new data
    public void updateUI(Movie[] movies) {
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


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSelection = position;
                //If connection available - load!
                if (isNetworkAvailable(MainActivity.this)) {
                    //hide Reload Button
                    reloadButton.setVisibility(View.GONE);
                    //connected - load data
                    new LoadFetchJSONmovies(position, new LoadFetchJSONCompleteListener()).execute();

                } else {
                    //hide Reload Button
                    reloadButton.setVisibility(View.VISIBLE);
                    //set error message on no connection
                    Toast.makeText(MainActivity.this, R.string.no_connection_toast, Toast.LENGTH_SHORT).show();
                }

                //new loadJSON().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return true;
    }

    //listener for JSON fetcher class
    public class LoadFetchJSONCompleteListener implements LoadFetchJSONmovies.AsyncTaskCompleteListener<Movie[]>{

        @Override
        public void onTaskBegin() {
            loadingIndicatorPB.setVisibility(View.VISIBLE);
        }

        @Override
        public void onTaskComplete(Movie[] movies) {
            //set Loading Icon INVISIBLE
            loadingIndicatorPB.setVisibility(View.INVISIBLE);
            //Update UI
            updateUI(movies);
        }
    }


}