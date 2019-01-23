package com.example.android.favoritemovies.asyncTask;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.favoritemovies.Constantes;
import com.example.android.favoritemovies.MovieTMDB;
import com.example.android.favoritemovies.MovieTMDBTrailer;
import com.example.android.favoritemovies.NetworkUtils;
import com.example.android.favoritemovies.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;

public class TrailerTask  extends AsyncTask<String , Void, String> {
    private static String TMDB_API_KEY;
    private MovieTMDB movie;
    private Context context;

    public TrailerTask(Context context, MovieTMDB movie) {
        this.context = context;
        this.movie = movie;
        TMDB_API_KEY = Constantes.API_KEY;
    }

    protected String doInBackground(String... params) {

        try{
            URL requestUrl = null;
            requestUrl = NetworkUtils.buildFilmUrl(movie.getId() + "/" + context.getString(R.string.path_videos), null, TMDB_API_KEY);

            return NetworkUtils.getResponseFromHttpUrl(requestUrl);
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @NonNull
    private MovieTMDBTrailer parseTrailersResult(JSONObject jsonObject) throws JSONException {

        return new MovieTMDBTrailer(
                jsonObject.getString(context.getString(R.string.trailers_id)),
                jsonObject.getString(context.getString(R.string.trailers_iso639)),
                jsonObject.getString(context.getString(R.string.trailers_iso3166)),
                jsonObject.getString(context.getString(R.string.trailers_key)),
                jsonObject.getString(context.getString(R.string.trailers_name)),
                jsonObject.getString(context.getString(R.string.trailers_site)),
                Integer.parseInt(jsonObject.getString(context.getString(R.string.trailers_size))),
                jsonObject.getString(context.getString(R.string.trailers_type)));
    }

    @Override
    protected void onPostExecute(String results) {
        movie.setTrailers(results);
    }

}
