package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);

    @Query("SELECT id From users")
    List<Integer> getUserId();

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Query("SELECT * FROM users WHERE email = :email")
    User getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE password = :password")
    User getUserByPassword(String password);

    @Query("SELECT * FROM users WHERE phone_number = :phoneNumber")
    User getUserByPhoneNumber(String phoneNumber);

    @Query("SELECT * FROM users WHERE first_name = :firstName")
    List<User> getUsersByFirstName(String firstName);

    @Query("SELECT * FROM users WHERE last_name = :lastName")
    List<User> getUsersByLastName(String lastName);
}