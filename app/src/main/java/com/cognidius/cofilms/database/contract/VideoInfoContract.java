package com.cognidius.cofilms.database.contract;

import android.provider.BaseColumns;

public class VideoInfoContract {
    private VideoInfoContract(){

    }

    public static class VideoInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "VideoInfo";
        public static final String COLUMN_NAME_VIDEOID = "VideoId"; // primary key
        public static final String COLUMN_NAME_VIDEOTITLE = "VideoTitle";
        public static final String COLUMN_NAME_VIDEOTYPE = "VideoType";
        public static final String COLUMN_NAME_BELONGTO = "BelongTo";// foreign key
        public static final String COLUMN_NAME_DISCRIPTION = "Disciption";
        public static final String COLUMN_NAME_URGENTLEVEL = "";
    }
}
