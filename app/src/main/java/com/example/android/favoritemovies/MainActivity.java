package com.example.android.favoritemovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private RecyclerView movieList;
    private MovieAdapter movieAdapter;
    private MovieRepository movieRepository;


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

        }
        return super.onOptionsItemSelected(item);
    }

    private boolean getMovies(String order) {
        movieRepository.getMovies(new OnGetMoviesCallback() {
            @Override
            public void onSuccess(List<MovieTMDB> movies) {
                movieAdapter = new MovieAdapter(MainActivity.this, movies);
                movieList.setAdapter(movieAdapter);
            }

            @Override
            public void onError() {
                Toast.makeText(MainActivity.this, "Por favor check sua conexão com a internet.", Toast.LENGTH_SHORT).show();
            }
        }, order);
        return true;
    }

}