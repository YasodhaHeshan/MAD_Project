package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "tickets",
        foreignKeys = {
                @ForeignKey(entity = User.class,
                            parentColumns = "id",
                            childColumns = "user_id",
                            onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = Bus.class,
                            parentColumns = "id",
                            childColumns = "bus_id",
                            onDelete = ForeignKey.CASCADE)
        },
        indices = {
                @Index(value = "user_id"),
                @Index(value = "bus_id")
        })
public class Ticket {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "user_id")
    public int userId;

    @ColumnInfo(name = "bus_id")
    public int busId;

    @ColumnInfo(name = "ticket_price")
    public int ticketPrice;

    @ColumnInfo(name = "ticket_number")
    public String ticketNumber;

    @ColumnInfo(name = "issue_date")
    public String issueDate;

    @ColumnInfo(name = "expiry_date")
    public String expiryDate;

    public Ticket(int id, int userId, int busId, int ticketPrice, String ticketNumber, String issueDate, String expiryDate) {
        this.id = id;
        this.userId = userId;
        this.busId = busId;
        this.ticketPrice = ticketPrice;
        this.ticketNumber = ticketNumber;
        this.issueDate = issueDate;
        this.expiryDate = expiryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
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

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}