package com.cognidius.cofilms.database.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cognidius.cofilms.database.room.dao.UserDao;

@Database(entities = {User.class}, version = 1)
public abstract class CoFilmsDatabase extends androidx.room.RoomDatabase {
    public abstract UserDao userDao();

}
