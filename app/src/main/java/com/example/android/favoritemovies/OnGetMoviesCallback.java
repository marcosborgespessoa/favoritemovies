package com.example.android.favoritemovies;

import java.util.List;

interface OnGetMoviesCallback {

    void onSuccess(List<MovieTMDB> movies);

    void onError();
}