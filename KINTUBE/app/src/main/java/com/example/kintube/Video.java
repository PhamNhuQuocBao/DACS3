package com.example.kintube;

public class Video {
    private Integer imageVideo;
    private String videoTitle;
    private String dateCreateAt;
    private Integer imageVideoUser;

    private String urlVideo;

    public Video(Integer imageVideo, String videoTitle, String dateCreateAt, Integer imageVideoUser , String urlVideo) {
        this.imageVideo = imageVideo;
        this.videoTitle = videoTitle;
        this.dateCreateAt = dateCreateAt;
        this.imageVideoUser = imageVideoUser;
        this.urlVideo = urlVideo;
    }

    public Integer getImageVideo() {
        return imageVideo;
    }

    public void setImageVideo(Integer imageVideo) {
        this.imageVideo = imageVideo;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getDateCreateAt() {
        return dateCreateAt;
    }

    public void setDateCreateAt(String dateCreateAt) {
        this.dateCreateAt = dateCreateAt;
    }

    public Integer getImageVideoUser() {
        return imageVideoUser;
    }

    public void setImageVideoUser(Integer imageVideoUser) {
        this.imageVideoUser = imageVideoUser;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }
}
