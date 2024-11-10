package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface RouteDao {
    @Insert
    void insert(Route route);

    @Query("SELECT * FROM Route WHERE id = :id")
    Route getRouteById(int id);
}