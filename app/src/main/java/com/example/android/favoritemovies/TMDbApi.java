package com.example.android.favoritemovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface TMDbApi {

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );


    @GET("movie/top_rated")
    Call<MovieResponse> getTopMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

}
