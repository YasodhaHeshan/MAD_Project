package com.example.mad_project.utils;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.User;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.Payment;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RebuildDatabase {
    private static final String TAG = "RebuildDatabase";
    private static final Executor executor = Executors.newSingleThreadExecutor();
    private static final String DB_NAME = "mad_project_db";

    public interface DatabaseCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void clearAndRebuildDatabase(Context context, boolean showLogs, DatabaseCallback callback) {
        executor.execute(() -> {
            try {
                // Close existing database connection
                AppDatabase.closeDatabase();

                // Delete the database file completely
                context.deleteDatabase(DB_NAME);
                if (showLogs) Log.d(TAG, "Existing database deleted");

                // Create new database instance
                AppDatabase db = AppDatabase.getDatabase(context);
                if (showLogs) Log.d(TAG, "New database created");

                // Insert sample data in a transaction
                db.runInTransaction(() -> {
                    try {
                        insertSampleData(db);
                        if (showLogs) Log.d(TAG, "Sample data inserted successfully");
                    } catch (Exception e) {
                        Log.e(TAG, "Error inserting sample data", e);
                        throw e; // Re-throw to rollback transaction
                    }
                });

                callback.onSuccess("Database rebuilt successfully");
            } catch (Exception e) {
                Log.e(TAG, "Error rebuilding database", e);
            }
        });
    }

    private static void insertSampleData(AppDatabase db) {
        db.runInTransaction(() -> {
            // 1. Insert Users first (no foreign key dependencies)
            User passenger1 = new User("Adithya Ekanayaka", "adithyasean@gmail.com", "1111111111",
                Validation.hashPassword("aaaaaa"), "user");
            User passenger2 = new User("Jane Smith", "ai@gmail.com", "1111111111",
                Validation.hashPassword("aaaaaa"), "user");
            User ownerUser = new User("Bob Wilson", "adithyasean@outlook.com", "+94761234567",
                Validation.hashPassword("aaaaaa"), "owner");
            User driverUser = new User("Dave Brown", "a@a.a", "+94751234567",
                Validation.hashPassword("aaaaaa"), "driver");

            long p1Id = db.userDao().insert(passenger1);
            long p2Id = db.userDao().insert(passenger2);
            long ownerId = db.userDao().insert(ownerUser);
            long driverId = db.userDao().insert(driverUser);

            // 2. Insert Bus Owner (depends on User)
            BusOwner busOwner = new BusOwner((int)ownerId, "Wilson Transport", "REG123456", "TAX789012");
            long busOwnerId = db.busOwnerDao().insert(busOwner);

            // 3. Insert Bus Driver (depends on User)
            BusDriver busDriver = new BusDriver((int)driverId, "DL123456", 
                System.currentTimeMillis() + 31536000000L, 5); // License expires in 1 year
            db.busDriverDao().insert(busDriver);

            // 4. Insert Buses (depends on Bus Owner)
            Bus bus1 = new Bus((int)busOwnerId, "NB-1234", "Volvo 9400", 40, 
                "WiFi, AC, USB Charging", true, "Colombo", "Kandy",
                6.933631, 79.855221, // Pettah Bus Station coordinates
                System.currentTimeMillis() + 3600000, // Departure in 1 hour
                System.currentTimeMillis() + 18000000, // Arrival in 5 hours
                2500.00); // Base fare

            Bus bus2 = new Bus((int)busOwnerId, "NB-5678", "Volvo 9400", 40,
                "WiFi, AC, USB Charging, Entertainment", true, "Colombo", "Galle",
                6.933631, 79.855221, // Pettah Bus Station coordinates
                System.currentTimeMillis() + 7200000, // Departure in 2 hours
                System.currentTimeMillis() + 14400000, // Arrival in 4 hours
                2000.00);

            long bus1Id = db.busDao().insert(bus1);
            long bus2Id = db.busDao().insert(bus2);

            // 5. Create tickets and payments together to maintain consistency
            // Ticket for passenger 1 on bus 1
            Ticket ticket1 = new Ticket((int)p1Id, (int)bus1Id, "A1", 
                System.currentTimeMillis() + 86400000, // Journey tomorrow
                "Colombo", "Kandy", "booked");
            long ticket1Id = db.ticketDao().insert(ticket1);

            // Payment for ticket 1
            Payment payment1 = new Payment((int)ticket1Id, (int)p1Id, (int)ticket1Id, 2625.00, 
                "CARD", "TXN" + System.currentTimeMillis(), "completed");
            long payment1Id = db.paymentDao().insert(payment1);

            // Update ticket with payment ID
            db.ticketDao().updateTicketPaymentId((int)ticket1Id, (int)payment1Id);

            // Ticket for passenger 2 on bus 2
            Ticket ticket2 = new Ticket((int)p2Id, (int)bus2Id, "B3",
                System.currentTimeMillis() + 172800000, // Journey in 2 days
                "Colombo", "Galle", "booked");
            long ticket2Id = db.ticketDao().insert(ticket2);

            // Payment for ticket 2
            Payment payment2 = new Payment((int)ticket2Id, (int)p2Id, (int)ticket2Id, 2100.00,
                "CARD", "TXN" + System.currentTimeMillis(), "completed");
            long payment2Id = db.paymentDao().insert(payment2);

            // Update ticket with payment ID
            db.ticketDao().updateTicketPaymentId((int)ticket2Id, (int)payment2Id);
        });
    }
}
