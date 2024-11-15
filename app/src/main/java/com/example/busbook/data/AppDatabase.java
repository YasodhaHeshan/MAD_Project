package com.example.busbook.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.busbook.controller.Converters;

@Database(entities = {User.class, Customer.class, BusOwner.class, BusDriver.class, Bus.class, Payment.class, Feedback.class, Ticket.class, Route.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CustomerDao customerDao();
    public abstract BusOwnerDao busOwnerDao();
    public abstract BusDriverDao busDriverDao();
    public abstract BusDao busDao();
    public abstract TicketDao ticketDao();
    public abstract PaymentDao paymentDao();
    public abstract FeedbackDao feedbackDao();
}