package com.example.android.favoritemovies;

import android.os.Parcel;
import android.os.Parcelable;


public class MovieTMDBReview implements Parcelable {

    private String id;
    private String author;
    private String content;
    private String url;

    public MovieTMDBReview(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    private MovieTMDBReview(Parcel in){
        this.id = in.readString();
        this.author = in.readString();
        this.content = in.readString();
        this.url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }


    public static final Parcelable.Creator<MovieTMDBReview> CREATOR = new Parcelable.Creator<MovieTMDBReview>() {
        @Override
        public MovieTMDBReview createFromParcel(Parcel in) {
            return new MovieTMDBReview(in);
        }

        @Override
        public MovieTMDBReview[] newArray(int size) {
            return new MovieTMDBReview[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
