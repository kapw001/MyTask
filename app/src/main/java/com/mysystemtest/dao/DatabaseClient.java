package com.mysystemtest.dao;

import android.content.Context;

import androidx.room.Room;

public class DatabaseClient {

    private Context mContext;
    private static DatabaseClient mInstance;

    private AppDatabase mAppDatabase;


    private DatabaseClient(Context mCtx) {
        this.mContext = mCtx;

        //creating the app database with Room database builder
        mAppDatabase = Room.databaseBuilder(mCtx, AppDatabase.class, "MyUsers")
                .fallbackToDestructiveMigration().build();
    }

    public static synchronized DatabaseClient getInstance(Context mCtx) {
        if (mInstance == null) {
            mInstance = new DatabaseClient(mCtx);
        }
        return mInstance;
    }

    public AppDatabase getAppDatabase() {
        return mAppDatabase;
    }

}
