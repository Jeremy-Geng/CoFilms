package com.cognidius.cofilms.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cognidius.cofilms.database.contract.CommunityAndUserContract;
import com.cognidius.cofilms.database.contract.CommunityInfoContract;
import com.cognidius.cofilms.database.contract.VideoInfoContract;

public class CommunityInfo extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "CommunityInfo.db";

    public CommunityInfo(Context context){
        super(context, DATABASE_NAME,null, DATABASE_VERSION);
    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + CommunityInfoContract.CommunityInfoEntry.TABLE_NAME + " (" +
            CommunityInfoContract.CommunityInfoEntry.COLUMN_NAME_COMMUNITYNAME + " TEXT PRIMARY KEY, " +
            CommunityInfoContract.CommunityInfoEntry.COLUMN_NAME_BELONGTO + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =  "DROP TABLE IF EXISTS " + CommunityInfoContract.CommunityInfoEntry.TABLE_NAME;

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
