package com.example.android.favoritemovies.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.favoritemovies.Constantes;
import com.example.android.favoritemovies.MovieTMDB;
import com.example.android.favoritemovies.MovieTMDBReview;
import com.example.android.favoritemovies.NetworkUtils;
import com.example.android.favoritemovies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;


public class ReviewTask extends AsyncTask<String , Void, String> {

    private static String TMDB_API_KEY;
    private MovieTMDB movie;
    private Context context;

    public ReviewTask(Context context, MovieTMDB movie) {
        this.context = context;
        this.movie = movie;
        TMDB_API_KEY = Constantes.API_KEY;
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL requestUrl = null;
            requestUrl = NetworkUtils.buildFilmUrl(movie.getId() + "/" + context.getString(R.string.path_reviews), null, TMDB_API_KEY);

            return NetworkUtils.getResponseFromHttpUrl(requestUrl);
        } catch (Exception e) {
            return null;
        }

    }

    @NonNull
    private MovieTMDBReview parseReviewResult(JSONObject jsonObject) throws JSONException {
        return new MovieTMDBReview(
                jsonObject.getString(context.getString(R.string.reviews_id)),
                jsonObject.getString(context.getString(R.string.reviews_author)),
                jsonObject.getString(context.getString(R.string.reviews_content)),
                jsonObject.getString(context.getString(R.string.reviews_url)));
    }

    @Override
    protected void onPostExecute(String results) {
        movie.setReviews(results);
    }
}
