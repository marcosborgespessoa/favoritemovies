/*
* Copyright (C) 2016 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.example.android.favoritemovies.dataModel;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.favoritemovies.dataModel.FavMovieContract.TaskEntry;


public class FavMovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "favMovieDB.db";

    private static final int VERSION = 3;

    FavMovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE "   + TaskEntry.TABLE_NAME + " (" +
                        TaskEntry._ID                 + " INTEGER PRIMARY KEY, " +
                        TaskEntry.COLUMN_MOVIE_ID     + " INTEGER, " +
                        TaskEntry.COLUMN_TITLE        + " TEXT NOT NULL, " +
                        TaskEntry.COLUMN_OVERVIEW     + " TEXT NOT NULL, " +
                        TaskEntry.COLUMN_RATING       + " REAL NOT NULL, " +
                        TaskEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                        TaskEntry.COLUMN_THUMBNAIL    + " TEXT NOT NULL, " +
                        TaskEntry.COLUMN_TRAILERS     + " TEXT, " +
                        TaskEntry.COLUMN_REVIEWS      + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskEntry.TABLE_NAME);
        onCreate(db);
    }
}
