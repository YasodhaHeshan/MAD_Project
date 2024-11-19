package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT id From users")
    List<Integer> getUserId();

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Insert
    void insertAll(User[] users);
}