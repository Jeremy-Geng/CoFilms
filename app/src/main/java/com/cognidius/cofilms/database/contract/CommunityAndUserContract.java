package com.cognidius.cofilms.database.contract;

import android.provider.BaseColumns;

public class CommunityAndUserContract {
    private CommunityAndUserContract(){

    }

    public static class CommunityAndUserEntry implements BaseColumns {
        public static final String TABLE_NAME = "CommunityAndUser";
        public static final String COLUMN_NAME_CONNECTIONID = "ConnectionId";
        public static final String COLUMN_NAME_COMMUNITYNAME = "CommunityName";
        public static final String COLUMN_NAME_USERNAME = "UserName";

    }
}
