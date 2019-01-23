package com.example.android.favoritemovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MovieTMDB implements Parcelable {

    private int id;
    private String title;
    private String posterPath;
    private String releaseDate;
    private String rating;
    private String overview;
    private String trailers;
    private String reviews;


    public MovieTMDB(int id, String title, String posterPath, String releaseDate, String rating, String overview) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.rating = rating;
        this.overview = overview;
    }

    public MovieTMDB(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.posterPath = in.readString();
        this.releaseDate = in.readString();
        this.rating = in.readString();
        this.overview = in.readString();
        this.trailers = in.readString();
        this.reviews = in.readString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeString(rating);
        dest.writeString(overview);
        dest.writeString(getTrailers());
        dest.writeString(getReviews());
    }

    public static final Parcelable.Creator<MovieTMDB> CREATOR = new Parcelable.Creator<MovieTMDB>() {
        @Override
        public MovieTMDB createFromParcel(Parcel in) {
            return new MovieTMDB(in);
        }

        @Override
        public MovieTMDB[] newArray(int size) {
            return new MovieTMDB[size];
        }
    };


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrailers() {
        return trailers;
    }

    public String getReviews() {
        return reviews;
    }

    public void setTrailers(String trailers) {
        this.trailers = trailers;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
}