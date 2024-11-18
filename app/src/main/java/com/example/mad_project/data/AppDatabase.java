package com.example.mad_project.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.mad_project.utils.Converters;

@Database(entities = {User.class, BusOwner.class, BusDriver.class, Bus.class, Payment.class, Schedule.class, Ticket.class, Route.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();

    public abstract BusOwnerDao busOwnerDao();

    public abstract BusDriverDao busDriverDao();

    public abstract BusDao busDao();

    public abstract TicketDao ticketDao();

    public abstract RouteDao routeDao();

    public abstract ScheduleDao scheduleDao();

    public abstract PaymentDao paymentDao();
}