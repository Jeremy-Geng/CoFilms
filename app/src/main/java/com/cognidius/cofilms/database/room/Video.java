package com.cognidius.cofilms.database.room;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "video")
public class Video {
    @PrimaryKey
    @NonNull
    private String videoId;

    @ColumnInfo(name = "videoTitle" )
    private String videoTitle = "defaultVideoTitle";

    @ColumnInfo(name = "videoType" )
    @NonNull
    private String videoType;

    @ColumnInfo(name = "belongTo" )
    @NonNull
    private String belongTo;

    @ColumnInfo(name = "description" )
    private String description;

    @ColumnInfo(name = "thumbnailUrl")
    private String thumbnailUrl;

    @ColumnInfo(name = "parentId")
    private String parentId;


    public Video(@NonNull String videoId, @NonNull String belongTo, @NonNull String videoType) {
        this.videoId = videoId;
        this.belongTo = belongTo;
        this.videoType = videoType;
    }

    @NonNull
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(@NonNull String videoId) {
        this.videoId = videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoType() {
        return videoType;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "Video{" +
                "videoId='" + videoId + '\'' +
                ", videoTitle='" + videoTitle + '\'' +
                ", videoType='" + videoType + '\'' +
                ", belongTo='" + belongTo + '\'' +
                ", description='" + description + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", parentId='" + parentId + '\'' +
                '}';
    }
}
