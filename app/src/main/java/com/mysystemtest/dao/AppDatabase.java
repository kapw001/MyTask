package com.mysystemtest.dao;

import com.mysystemtest.network.response.User;
import com.mysystemtest.network.response.UserDetails;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {User.class, UserDetails.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract UserDetailsDao userDetailsDao();
}