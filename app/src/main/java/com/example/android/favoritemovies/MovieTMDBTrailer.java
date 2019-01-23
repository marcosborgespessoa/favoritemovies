package com.example.android.favoritemovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieTMDBTrailer implements Parcelable {

    private String id;
    private String iso6391;
    private String iso31661;
    private String key;
    private String name;
    private String site;
    private int size;
    private String type;

    public MovieTMDBTrailer(String id, String iso6391, String iso31661, String key, String name, String site, int size, String type) {
        this.id = id;
        this.iso6391 = iso6391;
        this.iso31661 = iso31661;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    private MovieTMDBTrailer(Parcel in){
        this.id = in.readString();
        this.iso6391 = in.readString();
        this.iso31661 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(iso6391);
        dest.writeString(iso31661);
        dest.writeString(key);
        dest.writeString(name);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<MovieTMDBTrailer> CREATOR = new Parcelable.Creator<MovieTMDBTrailer>() {
        @Override
        public MovieTMDBTrailer createFromParcel(Parcel in) {
            return new MovieTMDBTrailer(in);
        }

        @Override
        public MovieTMDBTrailer[] newArray(int size) {
            return new MovieTMDBTrailer[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getIso6391() {
        return iso6391;
    }

    public String getIso31661() {
        return iso31661;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
