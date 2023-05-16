package com.example.kintube.Database;

import android.content.Context;
import android.support.annotation.NonNull;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.kintube.Model.Video.User.User;
import com.example.kintube.Model.Video.Video;

@Database(entities = {User.class}, version = 2)
public abstract class UserDatabase extends RoomDatabase {
    private static final String DB_NAME = "kintube.db";
    private static UserDatabase instance;

    public static synchronized UserDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    UserDatabase.class,
                    DB_NAME)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries().build();
        }
        return instance;
    }

    public abstract UserDAO userDAO();
}

