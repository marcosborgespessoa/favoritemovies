package com.example.android.favoritemovies;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.android.favoritemovies.data.FavMovieContract;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView movieList;
    private MovieAdapter movieAdapter;
    private MovieRepository movieRepository;
    //private CustomCursorAdapter mAdapter;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieRepository = MovieRepository.getInstance();

        movieList = findViewById(R.id.recyclerview_id);
        movieList.setLayoutManager(new GridLayoutManager(this,2));

        getMovies("top");
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
        if (id == R.id.menu_action_top) {
            return getMovies("top");

        } else  if (id == R.id.menu_action_pop){
            return getMovies("popular");

        } else  if (id == R.id.menu_action_fav){
            return getMovies("favorites");

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean getMovies(String order) {
        if (order.equals("favorites") ) {
            ArrayList<MovieTMDB> movies = getMoviesProvider();
            movieAdapter = new MovieAdapter(MainActivity.this, movies);
            movieList.setAdapter(movieAdapter);
        } else {
            movieRepository.getMovies(new OnGetMoviesCallback() {
                @Override
                public void onSuccess(List<MovieTMDB> movies) {
                    movieAdapter = new MovieAdapter(MainActivity.this, movies);
                    movieList.setAdapter(movieAdapter);
                }

                @Override
                public void onError() {
                    Toast.makeText(MainActivity.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
                }
            }, order);
        }
        return true;
    }

    @NonNull
    private ArrayList<MovieTMDB> getMoviesProvider() {
        Cursor cursor = getContentResolver().query(FavMovieContract.TaskEntry.CONTENT_URI,
                null,
                null,
                null,
                FavMovieContract.TaskEntry.COLUMN_MOVIE_ID);

        MovieTMDB movieTMDB;
        ArrayList<MovieTMDB> movies = new ArrayList<MovieTMDB>();

        if (cursor.moveToFirst()){
            do{
                movieTMDB = new MovieTMDB();
                movieTMDB.setId(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_MOVIE_ID)));
                movieTMDB.setTitle(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_TITLE)));
                movieTMDB.setReleaseDate(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_RELEASE_DATE)));
                movieTMDB.setRating(cursor.getFloat(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_RATING)));
                movieTMDB.setPosterPath(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_THUMBNAIL)));
                movieTMDB.setOverview(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_OVERVIEW)));
                movies.add(movieTMDB);
            }while(cursor.moveToNext());
        }
        return movies;
    }

}