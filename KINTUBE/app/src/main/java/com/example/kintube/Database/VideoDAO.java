package com.example.kintube.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.example.kintube.Model.Video.Video;
import java.util.List;

@Dao
public interface VideoDAO {
    @Insert
    void insertVideo(Video video);
    @Query("SELECT * FROM VIDEO")
    List<Video> getListVideo();

    @Query("SELECT * FROM VIDEO WHERE title = :search")
    List<Video> getListVideoSearch(String search);
}
