package com.cognidius.cofilms.database.room;

import android.content.Context;

import androidx.room.Room;

import com.cognidius.cofilms.database.room.dao.UserDao;

public class InitialDataBase {
    public static CoFilmsDatabase coFilmsDatabase;

    public static UserDao initialUserTable(Context context){
        coFilmsDatabase = Room.databaseBuilder(context, CoFilmsDatabase.class,"database").allowMainThreadQueries().build();
        return coFilmsDatabase.userDao();
    }




}
