package com.example.android.favoritemovies;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieRepository extends AppCompatActivity {


    private static MovieRepository repository;

    private final TMDbApi api;

    private MovieRepository(TMDbApi api) {
        this.api = api;
    }

    public static MovieRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constantes.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MovieRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }

    public void getMovies(final OnGetMoviesCallback callback, String order) {

        if (order.equals("popular")) {

            api.getPopularMovies(Constantes.API_KEY, Constantes.LANGUAGE, 1)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                MovieResponse movieResponse = response.body();
                                if (movieResponse != null && movieResponse.getMovies() != null) {
                                    callback.onSuccess(movieResponse.getMovies());
                                } else {
                                    callback.onError();
                                }
                            } else {
                                callback.onError();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                            callback.onError();
                        }
                    });

        } else if (order.equals("top")) {
            api.getTopMovies(Constantes.API_KEY, Constantes.LANGUAGE, 1)
                    .enqueue(new Callback<MovieResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                            if (response.isSuccessful()) {
                                MovieResponse movieResponse = response.body();
                                if (movieResponse != null && movieResponse.getMovies() != null) {
                                    callback.onSuccess(movieResponse.getMovies());
                                } else {
                                    callback.onError();
                                }
                            } else {
                                callback.onError();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {
                            callback.onError();
                        }
                    });
        }

        }
}