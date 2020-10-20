package com.cognidius.cofilms.database.room.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cognidius.cofilms.database.room.User;
import com.cognidius.cofilms.database.room.Video;

import java.util.List;

@Dao
public interface VideoDao {

    @Insert
    void insertAll(Video... videos);

    @Query("select * from video")
    List<Video> loadAllVideos();

    @Query("select * from video where belongTo = :userName and videoType = :videoType")
    List<Video> loadQuestionsRelatedToSpecificUser(String userName, String videoType);

    @Query("select * from video where videoType = :videoType")
    List<Video> loadAllQuestions(String videoType);



}
