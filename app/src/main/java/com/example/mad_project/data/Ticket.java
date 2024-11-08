package com.example.mad_project.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int bookingId;
    public String ticketNumber;
    public String issueDate;

    public Ticket(int id, int bookingId, String ticketNumber, String issueDate) {
        this.id = id;
        this.bookingId = bookingId;
        this.ticketNumber = ticketNumber;
        this.issueDate = issueDate;
    }
}