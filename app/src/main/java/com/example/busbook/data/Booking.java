package com.example.busbook.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Booking {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int busId;
    public int routeId;
    public String bookingDate;

    public Booking(int id, int userId, int busId, int routeId, String bookingDate) {
        this.id = id;
        this.userId = userId;
        this.busId = busId;
        this.routeId = routeId;
        this.bookingDate = bookingDate;
    }
}