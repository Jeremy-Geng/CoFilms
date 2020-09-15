package com.cognidius.cofilms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cognidius.cofilms.database.contract.UserInfoContract;
import com.cognidius.cofilms.database.contract.VideoInfoContract;

public class VideoInfo extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "VideoInfo.db";

    public VideoInfo(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + VideoInfoContract.VideoInfoEntry.TABLE_NAME + " (" +
            VideoInfoContract.VideoInfoEntry.COLUMN_NAME_VIDEOID + " TEXT PRIMARY KEY, " +
            VideoInfoContract.VideoInfoEntry.COLUMN_NAME_VIDEOTITLE + " TEXT," +
            VideoInfoContract.VideoInfoEntry.COLUMN_NAME_VIDEOTYPE + " TEXT, " +
            VideoInfoContract.VideoInfoEntry.COLUMN_NAME_BELONGTO + " TEXT," +
            VideoInfoContract.VideoInfoEntry.COLUMN_NAME_DISCRIPTION + " TEXT, " +
            VideoInfoContract.VideoInfoEntry.COLUMN_NAME_URGENTLEVEL + " INTEGER, "+
            "FOREIGN KEY(" + VideoInfoContract.VideoInfoEntry.COLUMN_NAME_BELONGTO + ")" +
            "REFERENCES  " + UserInfoContract.UserInfoEntry.TABLE_NAME + "(" + UserInfoContract.UserInfoEntry.COLUMN_NAME_USERNAME + "))"
            ;

    private static final String SQL_DELETE_ENTRIES =  "DROP TABLE IF EXISTS " + VideoInfoContract.VideoInfoEntry.TABLE_NAME;

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
