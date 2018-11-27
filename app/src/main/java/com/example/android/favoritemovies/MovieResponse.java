package com.example.android.favoritemovies;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

class MovieResponse {

    @SerializedName("results")
    @Expose
    private List<MovieTMDB> movies;

    public List<MovieTMDB> getMovies() {
        return movies;
    }

    public void setMovies(List<MovieTMDB> movies) {
        this.movies = movies;
    }

}
