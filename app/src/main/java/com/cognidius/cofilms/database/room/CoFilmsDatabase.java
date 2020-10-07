package com.cognidius.cofilms.database.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.cognidius.cofilms.database.room.dao.UserDao;
import com.cognidius.cofilms.database.room.dao.VideoDao;

@Database(entities = {User.class, Video.class}, version = 1)
public abstract class CoFilmsDatabase extends androidx.room.RoomDatabase {
    public abstract UserDao userDao();

    public abstract VideoDao videoDao();


}
