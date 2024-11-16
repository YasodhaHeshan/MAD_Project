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
    private int id;

    @ColumnInfo(name = "user_id")
    private int userId;

    @ColumnInfo(name = "bus_id")
    private int busId;
    
    @ColumnInfo(name = "ticket_id")
    private int ticketId;

    
    @ColumnInfo(name = "seat_number")
    private int seatNumber;

    
    @ColumnInfo(name = "price")
    private double price;

    
    @ColumnInfo(name = "booking_date")
    private String bookingDate;

    
    @ColumnInfo(name = "travel_date")
    private String travelDate;

    public Ticket(int userId, int busId, int ticketId,  int seatNumber, double price,  String bookingDate,  String travelDate) {
        this.userId = userId;
        this.busId = busId;
        this.ticketId = ticketId;
        this.seatNumber = seatNumber;
        this.price = price;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
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

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }


    public int getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber( int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }


    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate( String bookingDate) {
        this.bookingDate = bookingDate;
    }


    public String getTravelDate() {
        return travelDate;
    }

    public void setTravelDate( String travelDate) {
        this.travelDate = travelDate;
    }
}