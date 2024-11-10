package com.example.mad_project.data;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TicketDao {
    @Query("SELECT * FROM tickets")
    List<Ticket> getAllTickets();

    @Query("SELECT ticket_price FROM tickets WHERE customer_id = :customerId")
    int getTicketPriceByCustomerId(int customerId);
}