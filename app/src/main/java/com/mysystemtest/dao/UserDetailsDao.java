package com.mysystemtest.dao;

import com.mysystemtest.network.response.User;
import com.mysystemtest.network.response.UserDetails;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import io.reactivex.Flowable;

@Dao
public interface UserDetailsDao {

    @Query("SELECT * FROM userdetails WHERE id = :id")
    Flowable<UserDetails> getUserDetails(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserDetails userDetails);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<UserDetails> userDetails);

    @Delete
    void delete(UserDetails userDetails);

    @Update
    void update(UserDetails userDetails);

}
