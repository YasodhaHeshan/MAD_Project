package com.example.mad_project.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Feedback {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String comments;
    public int rating;

    public Feedback(int id, int userId, String comments, int rating) {
        this.id = id;
        this.userId = userId;
        this.comments = comments;
        this.rating = rating;
    }
}