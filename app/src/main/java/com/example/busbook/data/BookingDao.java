package com.example.busbook.data;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface BookingDao {
    @Insert
    void insert(Booking booking);

    @Query("SELECT * FROM Booking WHERE userId = :userId")
    List<Booking> getBookingsByUserId(int userId);
}