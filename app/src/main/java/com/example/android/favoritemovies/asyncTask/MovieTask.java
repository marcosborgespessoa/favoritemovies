package com.example.android.favoritemovies.asyncTask;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.AsyncTaskLoader;

import com.example.android.favoritemovies.Constantes;
import com.example.android.favoritemovies.MovieTMDB;
import com.example.android.favoritemovies.NetworkUtils;
import com.example.android.favoritemovies.dataModel.FavMovieContract;
import com.example.android.favoritemovies.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;


public class MovieTask extends AsyncTaskLoader<ArrayList<MovieTMDB>> {

    private int sortBy;
    private static String TMDB_API_KEY;
    private static final int POPULAR_LOADER_ID = 0;
    private static final int RATED_LOADER_ID = 1;
    private static final int FAVORITES_LOADER_ID = 2;

    ArrayList<MovieTMDB> movies = new ArrayList<MovieTMDB>();


    public MovieTask(Context ctx, int sortBy) {
        super(ctx);
        this.sortBy = sortBy;
        TMDB_API_KEY = Constantes.API_KEY;
    }

    @Override
    protected void onStartLoading() {
        if (movies.size() > 0) {
            deliverResult(movies);
        } else {
            forceLoad();
        }
    }

    @Override
    public ArrayList<MovieTMDB> loadInBackground() {
        try {
            loadMovies();
            return movies;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @NonNull
    private MovieTMDB parseMovieResult(JSONObject jsonMovieObject) throws JSONException {
        return new MovieTMDB(
                Integer.parseInt(jsonMovieObject.getString(getContext().getString(R.string.response_id))),
                jsonMovieObject.getString(getContext().getString(R.string.response_title)),
                jsonMovieObject.getString(getContext().getString(R.string.response_poster_path)),
                jsonMovieObject.getString(getContext().getString(R.string.response_release_date)),
                jsonMovieObject.getString(getContext().getString(R.string.response_vote_average)),
                jsonMovieObject.getString(getContext().getString(R.string.response_overview))
        );
    }

    private void loadMovies() throws Exception {
        URL requestUrl = null;

        switch (sortBy) {
            case POPULAR_LOADER_ID:
                loadFromURL(NetworkUtils.buildFilmUrl(getContext().getString(R.string.path_popular), null, TMDB_API_KEY));
                break;
            case RATED_LOADER_ID:
                loadFromURL(NetworkUtils.buildFilmUrl(getContext().getString(R.string.path_top_rated), null, TMDB_API_KEY));
                break;
            case FAVORITES_LOADER_ID:
                loadFromDB();
                break;
        }
    }

    private void loadFromDB(){
        Uri uri = FavMovieContract.TaskEntry.CONTENT_URI;
        ContentResolver resolver = getContext().getContentResolver();
        Cursor cursor = null;
        try {
            cursor = resolver.query(uri, null, null, null, null);
            MovieTMDB movieTMDB;
            if (cursor.moveToFirst()){
                do{
                     movieTMDB = new MovieTMDB(new Integer(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_MOVIE_ID))),
                            cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_TITLE)),
                            cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_THUMBNAIL)),
                            cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_RELEASE_DATE)),
                            cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_RATING)),
                            cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_OVERVIEW)));
                     movieTMDB.setTrailers(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_TRAILERS)));
                     movieTMDB.setReviews(cursor.getString(cursor.getColumnIndex(FavMovieContract.TaskEntry.COLUMN_REVIEWS)));
                    movies.add(movieTMDB);
                }while(cursor.moveToNext());
            }

        } finally {
            if(cursor != null)
                cursor.close();
        }
    }

    private void loadFromURL(URL requestUrl) throws JSONException, IOException {
        JSONObject jsonObject = new JSONObject(NetworkUtils.getResponseFromHttpUrl(requestUrl));
        JSONArray array = (JSONArray) jsonObject.get(getContext().getString(R.string.response_results));
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonMovieObject = array.getJSONObject(i);

            MovieTMDB movieTMDB = parseMovieResult(jsonMovieObject);

            ReviewTask reviewTask = new ReviewTask(getContext(),movieTMDB);
            reviewTask.execute();

            TrailerTask trailerTask = new TrailerTask(getContext(),movieTMDB);
            trailerTask.execute();

            movies.add(movieTMDB);
        }
    }

    public void deliverResult(ArrayList<MovieTMDB> movies) {
        this.movies = movies;
        super.deliverResult(movies);
    }

}
