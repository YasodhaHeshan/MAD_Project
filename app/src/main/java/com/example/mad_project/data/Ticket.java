package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tickets")
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "booking_id")
    public int bookingId;

    @ColumnInfo(name = "customer_id")
    public int customerId;

    @ColumnInfo(name = "ticket_price")
    public int ticketPrice;

    @ColumnInfo(name = "ticket_number")
    public String ticketNumber;

    @ColumnInfo(name = "issue_date")
    public String issueDate;

    public Ticket(int id, int bookingId, int customerId, int ticketPrice, String ticketNumber, String issueDate) {
        this.id = id;
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.ticketPrice = ticketPrice;
        this.ticketNumber = ticketNumber;
        this.issueDate = issueDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(int ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }
}