package com.cognidius.cofilms.database.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cognidius.cofilms.database.room.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insertAll(User... users);

    @Query("select * from user")
    List<User> loadAllUsers();



}
