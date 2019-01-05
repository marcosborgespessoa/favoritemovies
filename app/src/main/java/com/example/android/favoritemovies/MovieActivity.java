package com.example.android.favoritemovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
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
import com.example.android.favoritemovies.data.FavMoviesContentProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieActivity extends AppCompatActivity {

    private TextView tvOverview;
    private TextView tvRating;
    private TextView tvRelesedDate;
    private TextView tvFavorite;
    private ImageView imgThumbnail;
    private ImageView addButton;
    private ImageView removeButton;

    private int dbID;
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
        tvFavorite = findViewById(R.id.textView3);

        Intent intent = getIntent();

        dbID = intent.getExtras().getInt("bdID");
        movieID = intent.getExtras().getString("MovieID");
        title = intent.getExtras().getString("Title");
        overview = intent.getExtras().getString("Overview");
        rating = intent.getExtras().getFloat("Rating");
        releaseDate = intent.getExtras().getString("ReleaseDate");
        thumbnail = intent.getExtras().getString("PosterPath");

        tvTitle.setText(title);
        tvOverview.setText(overview);
        tvRating.setText(Float.toString(rating));
        tvRelesedDate.setText(releaseDate);
        Picasso.get().load(thumbnail).into(imgThumbnail);

        addButton = (ImageView) findViewById(R.id.fav_add);
        removeButton = (ImageView) findViewById(R.id.fav_remove);

        setFavoriteMovie();

        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(FavMovieContract.TaskEntry.COLUMN_MOVIE_ID, movieID);
                contentValues.put(FavMovieContract.TaskEntry.COLUMN_TITLE, title);
                contentValues.put(FavMovieContract.TaskEntry.COLUMN_OVERVIEW, overview);
                contentValues.put(FavMovieContract.TaskEntry.COLUMN_RATING, rating);
                contentValues.put(FavMovieContract.TaskEntry.COLUMN_RELEASE_DATE, releaseDate);
                String thumb = thumbnail.substring(Constantes.BASE_URL_IMAGE.length());
                contentValues.put(FavMovieContract.TaskEntry.COLUMN_THUMBNAIL, thumb);

                Uri uri = getContentResolver().insert(FavMovieContract.TaskEntry.CONTENT_URI, contentValues);


                if (uri != null) {
                    Toast.makeText(getBaseContext(), "Added to favorite", Toast.LENGTH_LONG).show();
                    //getContentResolver().notifyChange(uri, null);
                    addButton.setVisibility(ImageView.INVISIBLE);
                    removeButton.setVisibility(ImageView.VISIBLE);
                    tvFavorite.setText("REMOVE TO FAVORITE");
                }

            }
        });


        removeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Uri uri = FavMovieContract.TaskEntry.CONTENT_URI;
                uri = uri.buildUpon().appendPath(movieID).build();

                String mSelection = FavMovieContract.TaskEntry.COLUMN_MOVIE_ID+"=?";
                String[] mSelectionArgs = new String[]{movieID};
                getContentResolver().delete(uri, mSelection, mSelectionArgs);
                //getContentResolver().notifyChange(uri, null);

                if (uri != null) {
                    Toast.makeText(getBaseContext(), "Removed to favorite", Toast.LENGTH_LONG).show();
                    addButton.setVisibility(ImageView.VISIBLE);
                    removeButton.setVisibility(ImageView.INVISIBLE);
                    tvFavorite.setText("ADD TO FAVORITE");
                }

            }
        });
    }

    @NonNull
    private void setFavoriteMovie() {
        if (dbID == 0) {
            String mSelection = FavMovieContract.TaskEntry.COLUMN_MOVIE_ID+"=?";
            String[] mSelectionArgs = new String[]{movieID};

            Cursor cursor = getContentResolver().query(FavMovieContract.TaskEntry.CONTENT_URI,
                    null,
                    mSelection,
                    mSelectionArgs,
                    FavMovieContract.TaskEntry.COLUMN_MOVIE_ID);

            if (cursor.moveToFirst()){
                addButton.setVisibility(ImageView.INVISIBLE);
                removeButton.setVisibility(ImageView.VISIBLE);
                tvFavorite.setText("REMOVE TO FAVORITE");
            } else {
                addButton.setVisibility(ImageView.VISIBLE);
                removeButton.setVisibility(ImageView.INVISIBLE);
                tvFavorite.setText("ADD TO FAVORITE");
            }
        } else {
            addButton.setVisibility(ImageView.INVISIBLE);
            removeButton.setVisibility(ImageView.VISIBLE);
            tvFavorite.setText("REMOVE TO FAVORITE");
        }

    }

}
