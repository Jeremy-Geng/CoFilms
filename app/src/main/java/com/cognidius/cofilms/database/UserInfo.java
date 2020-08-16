package com.cognidius.cofilms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cognidius.cofilms.database.contract.UserInfoContract;

public class UserInfo extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UserInfo.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + UserInfoContract.UserInfoEntry.TABLE_NAME + " (" +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME + " TEXT PRIMARY KEY, " +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_PASSWORD + " TEXT," +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_BIRTHOFDATE + " TEXT, " +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_GENDER + " TEXT," +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_COUNTRY + " TEXT, " +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_NICKNAME + " TEXT, " +
            UserInfoContract.UserInfoEntry.COLUMN_NAME_ORGANIZATION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =  "DROP TABLE IF EXISTS " + UserInfoContract.UserInfoEntry.TABLE_NAME;

    public UserInfo(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION );


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
