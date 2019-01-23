package com.example.android.favoritemovies;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.android.favoritemovies.asyncTask.MovieTask;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieTMDB>>, MovieAdapter.MovieAdapterOnClickHandler {


    private RecyclerView movieList;
    private MovieAdapter movieAdapter;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageDisplay;

    private static final int POP_LOADER_ID = 0;
    private static final int TOP_LOADER_ID = 1;
    private static final int FAVORITES_LOADER_ID = 2;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieList = (RecyclerView) findViewById(R.id.recyclerview_id);
        movieList.setLayoutManager(new GridLayoutManager(this,2));

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        movieAdapter = new MovieAdapter(this, this);
        movieList.setAdapter(movieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        loading();

        if (isConected(this)){
            getSupportLoaderManager().initLoader(POP_LOADER_ID, null, this);
        } else {
            showErrorMessage();
        }
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        movieList.setVisibility(View.VISIBLE);
    }

    private void loading() {
        movieList.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public boolean isConected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();
            return ni != null && ni.isConnected();
        }
        return false;
    }

    private void showErrorMessage() {
        movieList.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(MovieTMDB movie) {
        Context context = this;
        Class detailActivity = MovieActivity.class;
        Intent intentDetailActivity = new Intent(context, detailActivity);
        intentDetailActivity.putExtra(getResources().getString(R.string.intent_detail_put_extra), movie);
        startActivity(intentDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (isConected(this)) {
            if (id == R.id.menu_action_top) {
                getSupportLoaderManager().restartLoader(TOP_LOADER_ID, null, this);
                return true;
            } else if (id == R.id.menu_action_pop) {
                getSupportLoaderManager().restartLoader(POP_LOADER_ID, null, this);
                return true;
            } else if (id == R.id.menu_action_fav) {
                getSupportLoaderManager().restartLoader(FAVORITES_LOADER_ID, null, this);
                return true;
            }
        } else {
            showErrorMessage();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieTMDB>> loader, ArrayList<MovieTMDB> movieTMDBS) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        movieAdapter.setData(movieTMDBS);
        if (null == movieTMDBS) {
            showErrorMessage();
        } else {
            showDataView();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieTMDB>> loader) {
        loader = null;
    }

    private void invalidateData() {
        movieAdapter.setData(null);
    }

    public Loader<ArrayList<MovieTMDB>>onCreateLoader(final int id, final Bundle loaderArgs) {

        return new MovieTask(this, id);

    }


}