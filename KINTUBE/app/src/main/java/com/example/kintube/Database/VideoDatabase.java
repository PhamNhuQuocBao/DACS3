package com.example.kintube.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.kintube.Model.Video.Video;

@Database(entities = {Video.class}, version = 2)
public abstract class VideoDatabase extends RoomDatabase {
    private static final String DB_NAME = "kintube.db";
    private static VideoDatabase instance;

    public static synchronized VideoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    VideoDatabase.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract VideoDAO videoDAO();
}
