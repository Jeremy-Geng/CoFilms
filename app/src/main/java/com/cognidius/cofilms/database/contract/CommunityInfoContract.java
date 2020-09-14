package com.cognidius.cofilms.database.contract;

import android.provider.BaseColumns;

public class CommunityInfoContract {
    private CommunityInfoContract(){

    }

    public static class CommunityInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "CommunityInfo";
        public static final String COLUMN_NAME_COMMUNITYNAME = "CommunityName";
        public static final String COLUMN_NAME_BELONGTO = "BelongTo";
    }
}
