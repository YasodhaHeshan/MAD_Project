package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FeedbackDao {

    @Insert
    void insert(Feedback feedback);

    @Update
    void update(Feedback feedback);

    @Delete
    void delete(Feedback feedback);

    @Query("SELECT * FROM Feedbacks WHERE id = :id")
    Feedback getFeedbackById(int id);

    @Query("SELECT * FROM Feedbacks")
    List<Feedback> getAllFeedback();
}