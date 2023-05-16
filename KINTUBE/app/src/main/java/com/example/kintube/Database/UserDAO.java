package com.example.kintube.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kintube.Model.Video.User.User;

import java.util.List;

@Dao
public interface UserDAO {
    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM USER")
    List<User> getAllUser();

    @Query("SELECT * FROM USER WHERE email = :email AND password = :password")
    List<User> getUser(String email, String password);

    @Query("SELECT * FROM USER WHERE email = :email")
    List<User> getUserByEmail(String email);

    @Query("SELECT * FROM USER WHERE id = :id")
    List<User> getUserById(int id);
}
