package com.example.android.favoritemovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

@SuppressWarnings("ALL")
public class MovieActivity extends AppCompatActivity {

    private TextView tvOverview;
    private TextView tvRating;
    private TextView tvRelesedDate;
    private ImageView imgThumbnail;
    private static final String BASE_URL = "http://image.tmdb.org/t/p/w185";

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

        String title = intent.getExtras().getString("Title");
        String overview = intent.getExtras().getString("Overview");
        float rating = intent.getExtras().getFloat("Rating");
        String releaseDate = intent.getExtras().getString("ReleaseDate");
        String thumbnail = intent.getExtras().getString("PosterPath");

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvRating.setText("Rating: " + Float.toString(rating));
        tvRelesedDate.setText("Release date: " + releaseDate);
        Picasso.get().load(thumbnail).into(imgThumbnail);
    }
}
