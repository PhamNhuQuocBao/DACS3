package com.example.kintube;

public class Video {
    private Integer imageVideo;
    private String videoTitle;
    private Integer imageVideoUser;

    public Video(Integer imageVideo, String videoTitle, Integer imageVideoUser) {
        this.imageVideo = imageVideo;
        this.videoTitle = videoTitle;
        this.imageVideoUser = imageVideoUser;
    }

    public Integer getImageVideo() {
        return imageVideo;
    }

    public void setImageVideo() {
        this.imageVideo = imageVideo;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public Integer getImageVideoUser() {
        return imageVideoUser;
    }

    public void setImageVideoUser(Integer imageVideoUser) {
        this.imageVideoUser = imageVideoUser;
    }
}
