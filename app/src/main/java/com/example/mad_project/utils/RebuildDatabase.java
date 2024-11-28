package com.example.mad_project.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
    private static Handler mainHandler = null;
    private static final String DB_NAME = "mad_project_db";

    private static Handler getMainHandler() {
        if (mainHandler == null) {
            // Ensure we're on the main thread when creating the handler
            if (Looper.myLooper() == Looper.getMainLooper()) {
                mainHandler = new Handler(Looper.getMainLooper());
            } else {
                // Post handler creation to main thread
                try {
                    Looper.prepare();
                    mainHandler = new Handler(Looper.getMainLooper());
                    Looper.loop();
                } catch (Exception e) {
                    Log.e(TAG, "Error creating handler", e);
                }
            }
        }
        return mainHandler;
    }

    public interface DatabaseCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void clearAndRebuildDatabase(Context context, boolean showLogs, DatabaseCallback callback) {
        executor.execute(() -> {
            try {
                // Close existing database connection
                AppDatabase.closeDatabase();

                // Delete database file
                context.deleteDatabase(DB_NAME);

                // Create new database instance
                AppDatabase db = Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build();

                // Insert sample data
                insertSampleData(db);

                // Post callback to main thread
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> 
                        callback.onSuccess("Database rebuilt successfully"));
                }

            } catch (Exception e) {
                Log.e(TAG, "Error rebuilding database", e);
                if (callback != null) {
                    new Handler(Looper.getMainLooper()).post(() -> 
                        callback.onError(e.getMessage()));
                }
            }
        });
    }

    private static void insertSampleData(AppDatabase db) {
        db.runInTransaction(() -> {
            // 1. Create Users with initial points
            User passenger1 = new User("Adithya Ekanayaka", "adithyasean@gmail.com", "1111111111",
                Validation.hashPassword("aaaaaa"), "user");
            passenger1.setPoints(50000); // Initial points

            User passenger2 = new User("Jane Smith", "ai@gmail.com", "1111111111",
                Validation.hashPassword("aaaaaa"), "user");
            passenger2.setPoints(30000);

            User ownerUser = new User("Bob Wilson", "adithyasean@outlook.com", "+94761234567",
                Validation.hashPassword("aaaaaa"), "owner");
            ownerUser.setPoints(10000);

            User driverUser = new User("Dave Brown", "a@a.a", "+94751234567",
                Validation.hashPassword("aaaaaa"), "driver");
            driverUser.setPoints(10000);

            long p1Id = db.userDao().insert(passenger1);
            long p2Id = db.userDao().insert(passenger2);
            long ownerId = db.userDao().insert(ownerUser);
            long driverId = db.userDao().insert(driverUser);

            // 2. Create Bus Owner and their buses
            BusOwner busOwner = new BusOwner((int)ownerId, "Wilson Transport", "REG123456", "TAX789012");
            long busOwnerId = db.busOwnerDao().insert(busOwner);

            Bus bus1 = new Bus((int)busOwnerId, "NB-1234", "Volvo 9400", 40, 
                "WiFi, AC, USB Charging", true, "Colombo", "Kandy",
                6.933631, 79.855221,
                System.currentTimeMillis() + 3600000,
                System.currentTimeMillis() + 18000000,
                2500); // Base points instead of fare

            Bus bus2 = new Bus((int)busOwnerId, "NB-5678", "Volvo 9400", 40,
                "WiFi, AC, USB Charging, Entertainment", true, "Colombo", "Galle",
                6.933631, 79.855221,
                System.currentTimeMillis() + 7200000,
                System.currentTimeMillis() + 14400000,
                2000); // Base points

            long bus1Id = db.busDao().insert(bus1);
            long bus2Id = db.busDao().insert(bus2);

            // 3. Create Driver record
            BusDriver busDriver = new BusDriver((int)driverId, "DL123456", 
                System.currentTimeMillis() + 31536000000L, 5);
            db.busDriverDao().insert(busDriver);

            // 4. Create complete booking records for Passenger 1
            // Booking 1: Colombo to Kandy
            Ticket ticket1 = new Ticket(
                (int)p1Id, 
                (int)bus1Id, 
                "A1",
                System.currentTimeMillis() + 86400000,
                "Colombo", 
                "Kandy",
                "booked"
            );
            long ticket1Id = db.ticketDao().insert(ticket1);

            Payment payment1 = new Payment(
                (int)ticket1Id,
                (int)p1Id,
                (int)ticket1Id,
                2625 // Base points (2500) + 5% booking fee
            );
            long payment1Id = db.paymentDao().insert(payment1);
            
            // Update ticket with payment and deduct points
            db.ticketDao().updateTicketPaymentId((int)ticket1Id, (int)payment1Id);
            db.userDao().deductPoints((int)p1Id, 2625);

            // 5. Create complete booking records for Passenger 2
            // Booking 2: Colombo to Galle
            Ticket ticket2 = new Ticket(
                (int)p2Id,
                (int)bus2Id,
                "B3",
                System.currentTimeMillis() + 172800000,
                "Colombo",
                "Galle",
                "booked"
            );
            long ticket2Id = db.ticketDao().insert(ticket2);

            Payment payment2 = new Payment(
                (int)ticket2Id,
                (int)p2Id,
                (int)ticket2Id,
                2100 // Base points (2000) + 5% booking fee
            );
            long payment2Id = db.paymentDao().insert(payment2);

            // Update ticket with payment and deduct points
            db.ticketDao().updateTicketPaymentId((int)ticket2Id, (int)payment2Id);
            db.userDao().deductPoints((int)p2Id, 2100);
        });
    }
}
