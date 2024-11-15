package com.example.busbook.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TicketDao {
    @Insert
    void insert(Ticket ticket);

    @Query("SELECT * FROM tickets")
    List<Ticket> getAllTickets();

    @Query("SELECT * FROM tickets WHERE user_id = :userId")
    int getTicketPriceByUserId(int userId);

    @Query("SELECT * FROM tickets WHERE bus_id = :busId")
    int getTicketPriceByBusId(int busId);
}