package com.example.mad_project.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

import java.util.List;

@Entity(tableName = "customers")
public class Customer extends User {

    @ColumnInfo(name = "address")
    private String address;

    @ColumnInfo(name = "payment_method")
    private String paymentMethod;

    @ColumnInfo(name = "loyalty_points")
    private int loyaltyPoints;

    @ColumnInfo(name = "booking_history")
    private List<String> bookingHistory;

    @ColumnInfo(name = "preferred_seat_type")
    private String preferredSeatType;

    @ColumnInfo(name = "bus_id")
    private int busId;

    public Customer(int id, String firstName, String lastName, String email, String phoneNumber, String password, String address, String paymentMethod, int loyaltyPoints, List<String> bookingHistory, String preferredSeatType, int busId) {
        super(id, firstName, lastName, email, phoneNumber, password);
        this.address = address;
        this.paymentMethod = paymentMethod;
        this.loyaltyPoints = loyaltyPoints;
        this.bookingHistory = bookingHistory;
        this.preferredSeatType = preferredSeatType;
        this.busId = busId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(int loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public List<String> getBookingHistory() {
        return bookingHistory;
    }

    public void setBookingHistory(List<String> bookingHistory) {
        this.bookingHistory = bookingHistory;
    }

    public String getPreferredSeatType() {
        return preferredSeatType;
    }

    public void setPreferredSeatType(String preferredSeatType) {
        this.preferredSeatType = preferredSeatType;
    }

    public int getBusId() {
        return busId;
    }

    public void setBusId(int busId) {
        this.busId = busId;
    }
}