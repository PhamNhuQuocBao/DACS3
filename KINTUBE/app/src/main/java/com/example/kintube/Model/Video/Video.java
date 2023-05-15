package com.example.kintube.Model.Video;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.kintube.Model.Video.User.User;

@Entity(tableName = "VIDEO")
public class Video {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    private String title;
    private String description;
    private String file_path;
    private String upload_date;

    @ColumnInfo(name = "user_id")
    private int user_id;
    private String imageVideo;

    public Video() {
    }
    public String getImageVideo() {
        return imageVideo;
    }

    public void setImageVideo(String imageVideo) {
        this.imageVideo = imageVideo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
