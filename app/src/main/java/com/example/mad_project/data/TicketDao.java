package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insert(Ticket ticket);

    @Update
    void update(Ticket ticket);

    @Insert
    void insertAll(Ticket[] tickets);

    @Query("SELECT price FROM tickets WHERE bus_id = :busId")
    double getTicketPriceByBusId(int busId);
}