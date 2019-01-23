package com.example.android.favoritemovies;

import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.favoritemovies.dataModel.FavMovieContract;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieActivity extends AppCompatActivity {

    private MovieTMDB movieTMDB;
    private ImageView addButton;
    private ImageView removeButton;
    private TextView tvFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        movieTMDB = (MovieTMDB) getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_detail_put_extra));


        ((TextView) findViewById(R.id.txtTitle)).setText(movieTMDB.getTitle());
        ((TextView) findViewById(R.id.txtOverview)).setText(movieTMDB.getOverview());
        ((TextView) findViewById(R.id.txtRating)).setText(movieTMDB.getRating() + "/10");
        ((TextView) findViewById(R.id.txtReleaseDate)).setText(movieTMDB.getReleaseDate());

        try {
            Picasso.get().load(NetworkUtils.buildImageFilmUrl(movieTMDB.getPosterPath()).toString()).into((ImageView) findViewById(R.id.movieThumbnail_id));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            loadTrailers(movieTMDB.getTrailers());
            loadReviews(movieTMDB.getReviews());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addButton = (ImageView) findViewById(R.id.fav_add);
        removeButton = (ImageView) findViewById(R.id.fav_remove);
        tvFavorite = (TextView) findViewById(R.id.textView3);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    addFavorites();
                }
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                button.setSelected(!button.isSelected());
                if (button.isSelected()) {
                    removeFavorites();
                }
            }
        });

        checkFavorites();

        getSupportActionBar().setHomeButtonEnabled(true);

    }


    private void loadReviews(String reviews) throws JSONException {
        if (null == reviews) {
            reviews = "";
        }
        LinearLayout reviewList = findViewById(R.id.review_list);

        JSONObject jsonList = new JSONObject(reviews);
        JSONArray array = (JSONArray) jsonList.get(getString(R.string.response_results));
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View reviewView = inflater.inflate(R.layout.review_list, null);
            ((TextView) reviewView.findViewById(R.id.review_author)).setText(jsonObject.getString(getString(R.string.reviews_author)));
            ((TextView) reviewView.findViewById(R.id.review_content)).setText(jsonObject.getString(getString(R.string.reviews_content)));
            reviewList.addView(reviewView);
        }
    }


    private void loadTrailers(String trailers) throws JSONException {
        if (null == trailers) {
            trailers = "";
        }
        LinearLayout trailerList = findViewById(R.id.trailer_list);

        JSONObject jsonList = new JSONObject(trailers);
        final JSONArray array = (JSONArray) jsonList.get(getString(R.string.response_results));
        for (int i = 0; array != null && i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View trailerView = inflater.inflate(R.layout.trailer_film, null);
            ((ImageView) trailerView.findViewById(R.id.trailer_button)).setImageResource(R.drawable.youtube_play);
            ((TextView) trailerView.findViewById(R.id.trailer_title)).setText(jsonObject.getString(getString(R.string.trailers_name)));
            final String trailerKey = jsonObject.getString(getString(R.string.trailers_key));
            trailerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent youTubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailerKey));
                    Intent webIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(String.format(getString(R.string.youtube_link), trailerKey)));
                    try {
                        startActivity(youTubeIntent);
                    } catch (ActivityNotFoundException ex) {
                        startActivity(webIntent);
                    }
                }
            });

            trailerList.addView(trailerView);
        }

    }

    private void addFavorites() {
        Uri uri = FavMovieContract.TaskEntry.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.clear();
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_MOVIE_ID, movieTMDB.getId());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_TITLE, movieTMDB.getTitle());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_OVERVIEW, movieTMDB.getOverview());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_RATING, movieTMDB.getRating());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_RELEASE_DATE, movieTMDB.getReleaseDate());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_THUMBNAIL, movieTMDB.getPosterPath());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_TRAILERS, movieTMDB.getTrailers());
        contentValues.put(FavMovieContract.TaskEntry.COLUMN_REVIEWS, movieTMDB.getReviews());

        if (uri != null) {
            Toast.makeText(getBaseContext(), "Movie added to favorite", Toast.LENGTH_LONG).show();
            addButton.setVisibility(ImageView.INVISIBLE);
            removeButton.setVisibility(ImageView.VISIBLE);
            tvFavorite.setText("REMOVE TO FAVORITE");
        }
        resolver.insert(uri, contentValues);
    }

    private void removeFavorites() {
        Uri uri = FavMovieContract.TaskEntry.CONTENT_URI;
        ContentResolver resolver = getContentResolver();
        resolver.delete(uri,FavMovieContract.TaskEntry.COLUMN_MOVIE_ID + " = ?",new String[]{movieTMDB.getId()+""});
        Toast.makeText(getBaseContext(), "Removed to favorite", Toast.LENGTH_LONG).show();
        addButton.setVisibility(ImageView.VISIBLE);
        removeButton.setVisibility(ImageView.INVISIBLE);
        tvFavorite.setText("ADD TO FAVORITE");
    }


    private boolean checkFavorites(){
        Uri uri = FavMovieContract.TaskEntry.buildMovieUri(movieTMDB.getId());
        ContentResolver resolver = getContentResolver();
        Cursor cursor = null;
        String[] args = new String[]{new Long(movieTMDB.getId()).toString()};
        try {
            cursor = resolver.query(uri, null, "movie_id", args, null);
            if (cursor != null && cursor.moveToFirst()){
                addButton.setVisibility(ImageView.INVISIBLE);
                removeButton.setVisibility(ImageView.VISIBLE);
                tvFavorite.setText("REMOVE TO FAVORITE");
            } else {
                addButton.setVisibility(ImageView.VISIBLE);
                removeButton.setVisibility(ImageView.INVISIBLE);
                tvFavorite.setText("ADD TO FAVORITE");
            }
        } finally {
            if(cursor != null)
                cursor.close();
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.
                finish();
                //startActivity(new Intent(this, MainActivity.class));
                break;
            default:break;
        }
        return true;
    }

    @Override
    public void onBackPressed(){ //Botão BACK padrão do android
        finish();
        //startActivity(new Intent(this, MainActivity.class));

        return;
    }


}
