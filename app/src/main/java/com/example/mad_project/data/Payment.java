package com.example.mad_project.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Payment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int bookingId;
    public double amount;
    public String paymentDate;
    public String paymentMethod;

    public Payment(int id, int bookingId, double amount, String paymentDate, String paymentMethod) {
        this.id = id;
        this.bookingId = bookingId;
        this.amount = amount;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
    }
}