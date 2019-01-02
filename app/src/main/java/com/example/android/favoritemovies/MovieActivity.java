package com.example.android.favoritemovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.android.favoritemovies.data.FavMovieContract;
import com.squareup.picasso.Picasso;

public class MovieActivity extends AppCompatActivity {

    private TextView tvOverview;
    private TextView tvRating;
    private TextView tvRelesedDate;
    private ImageView imgThumbnail;
    private ToggleButton favMovieButton;
    private String movieID;
    private String title;
    private String overview;
    private float rating;
    private String releaseDate;
    private String thumbnail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        TextView tvTitle = findViewById(R.id.txtTitle);
        tvOverview = findViewById(R.id.txtOverview);
        tvRating = findViewById(R.id.txtRating);
        tvRelesedDate = findViewById(R.id.txtReleaseDate);
        imgThumbnail = findViewById(R.id.movieThumbnail_id);

        Intent intent = getIntent();

        movieID = intent.getExtras().getString("MovieID");
        title = intent.getExtras().getString("Title");
        overview = intent.getExtras().getString("Overview");
        rating = intent.getExtras().getFloat("Rating");
        releaseDate = intent.getExtras().getString("ReleaseDate");
        thumbnail = intent.getExtras().getString("PosterPath");

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvRating.setText("Rating: " + Float.toString(rating));
        tvRelesedDate.setText("Release date: " + releaseDate);
        Picasso.get().load(thumbnail).into(imgThumbnail);

        favMovieButton = (ToggleButton) findViewById(R.id.btnFavButton);
        favMovieButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    favMovieButton.setTextOff("+");
                else
                    favMovieButton.setTextOn("-");
            }
        });
    }


    public void onClickAddTask(View view) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_MOVIE_ID, movieID);
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_TITLE, title);
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_OVERVIEW, overview);
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_RATING, rating);
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_RELEASE_DATE, releaseDate);
        String thumb = thumbnail.substring(Constantes.BASE_URL_IMAGE.length());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_THUMBNAIL, thumb);

        Uri uri = getContentResolver().insert(FavMovieContract.TaskEntry.CONTENT_URI, contentValues);

        if(uri != null) {
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }

        finish();

    }

}
