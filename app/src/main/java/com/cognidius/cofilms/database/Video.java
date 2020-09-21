package com.cognidius.cofilms.database;

public class Video {
    private String videoId;
    private String videoTitle;
    private String videoType;
    private String belongTo;
    private String description;
    private String thumbnailUrl;

    public Video(String videoId, String videoTitle, String videoType) {
        this.videoId = videoId;
        this.videoTitle = videoTitle;
        this.videoType = videoType;
    }

    public String getVideoId() {
        return videoId;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public String getVideoType() {
        return videoType;
    }

    public String getBelongTo() {
        return belongTo;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailurl() { return thumbnailUrl; }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public void setVideoType(String videoType) {
        this.videoType = videoType;
    }

    public void setBelongTo(String belongTo) {
        this.belongTo = belongTo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setThumbnailurl(String thumbnailurl) { this.thumbnailUrl = thumbnailurl; }
}
