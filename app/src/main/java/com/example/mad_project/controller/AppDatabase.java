package com.example.mad_project.controller;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.data.Customer;
import com.example.mad_project.data.CustomerDao;
import com.example.mad_project.data.Feedback;
import com.example.mad_project.data.FeedbackDao;
import com.example.mad_project.data.Payment;
import com.example.mad_project.data.PaymentDao;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;

@Database(entities = {User.class, Customer.class, BusOwner.class, BusDriver.class, Bus.class, Payment.class, Feedback.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract CustomerDao customerDao();
    public abstract BusOwnerDao busOwnerDao();
    public abstract BusDriverDao busDriverDao();
    public abstract BusDao busDao();
    public abstract PaymentDao paymentDao();
    public abstract FeedbackDao feedbackDao();

    // Define migrations
    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Perform necessary migration steps
            database.execSQL("CREATE INDEX index_buses_bus_owner_id ON buses(bus_owner_id)");
            database.execSQL("CREATE INDEX index_buses_bus_driver_id ON buses(bus_driver_id)");
        }
    };
}