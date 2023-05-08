package com.example.kintube;

public class Video {
    private Integer imageVideo;
    private Integer imageVideoUser;
    private String id;
    private String title;
    private String description;
    private String duration;
    private String file_path;
    private String upload_date;
    private String user_id;

    public Video() {
    }
    public Integer getImageVideo() {
        return imageVideo;
    }

    public void setImageVideo(Integer imageVideo) {
        this.imageVideo = imageVideo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getImageVideoUser() {
        return imageVideoUser;
    }

    public void setImageVideoUser(Integer imageVideoUser) {
        this.imageVideoUser = imageVideoUser;
    }

}
