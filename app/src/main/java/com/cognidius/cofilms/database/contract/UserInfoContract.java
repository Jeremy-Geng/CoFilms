package com.cognidius.cofilms.database.contract;

import android.provider.BaseColumns;

import com.cognidius.cofilms.database.UserInfo;

public class UserInfoContract {
    private UserInfoContract(){

    }

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
