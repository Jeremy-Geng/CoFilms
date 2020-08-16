package com.cognidius.cofilms.database.contract;

import android.provider.BaseColumns;

import com.cognidius.cofilms.database.UserInfo;

public class UserInfoContract {
    private UserInfoContract(){

    }

    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE " + UserInfoEntry.TABLE_NAME + " (" +
            UserInfoEntry.COLUMN_NAME_USERNAME + " TEXT PRIMARY KEY, " +
            UserInfoEntry.COLUMN_NAME_PASSWORD + " TEXT," +
            UserInfoEntry.COLUMN_NAME_BIRTHOFDATE + " TEXT, " +
            UserInfoEntry.COLUMN_NAME_GENDER + " TEXT" +
            UserInfoEntry.COLUMN_NAME_COUNTRY + " TEXT, " +
            UserInfoEntry.COLUMN_NAME_NICKNAME + " TEXT, " +
            UserInfoEntry.COLUMN_NAME_ORGANIZATION + " TEXT)"
            ;




    public static class UserInfoEntry implements BaseColumns{
        public static final String TABLE_NAME = "UserInfo";
        public static final String COLUMN_NAME_USERNAME = "Username";
        public static final String COLUMN_NAME_PASSWORD = "Password";
        public static final String COLUMN_NAME_NICKNAME = "Nickname";
        public static final String COLUMN_NAME_COUNTRY = "Country";
        public static final String COLUMN_NAME_GENDER = "Gender";
        public static final String COLUMN_NAME_ORGANIZATION = "Organization";
        public static final String COLUMN_NAME_BIRTHOFDATE = "BirthOfDate";
    }
}
