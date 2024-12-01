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
import com.example.mad_project.data.Notification;
import com.example.mad_project.data.Location;

import java.util.List;
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

    public interface RebuildCallback {
        void onSuccess(String message);
        void onError(String error);
    }

    public static void clearAndRebuildDatabase(Context context, boolean async, RebuildCallback callback) {
        Runnable rebuildTask = () -> {
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
        };

        if (async) {
            executor.execute(rebuildTask);
        } else {
            rebuildTask.run();
        }
    }

    private static void insertSampleData(AppDatabase db) {
        db.runInTransaction(() -> {
            // 1. Create Users with different roles and initial points
            User passenger1 = new User("Adithya Ekanayaka", "adithyasean@gmail.com", "+94702967909",
                Validation.hashPassword("aaaaaa"), "user");
            passenger1.setPoints(50000);

            User passenger2 = new User("Adithya Ekanayaka", "adithyasean@outlook.com", "+94767007909",
                Validation.hashPassword("aaaaaa"), "user");
            passenger2.setPoints(30000);

            User passenger3 = new User("Yasodha Heshan", "yasodhaheshan2002@gmail.com", "+94111111111",
                Validation.hashPassword("aaaaaa"), "user");
            passenger3.setPoints(45000);

            User ownerUser1 = new User("Senith Nimsara", "senith2002n@gmail.com", "+94761234567",
                Validation.hashPassword("aaaaaa"), "owner");
            ownerUser1.setPoints(10000);

            User ownerUser2 = new User("Jalith Chamikara", "premjalith.@gmail.com", "+94762345678",
                Validation.hashPassword("aaaaaa"), "owner");
            ownerUser2.setPoints(15000);

            User driverUser1 = new User("Dave Brown", "ai@gmail.com", "+94751234567",
                Validation.hashPassword("aaaaaa"), "driver");
            driverUser1.setPoints(10000);

            User driverUser2 = new User("John Connor", "a@a.a", "+94752345678",
                Validation.hashPassword("aaaaaa"), "driver");
            driverUser2.setPoints(12000);

            // Insert all users
            long p1Id = db.userDao().insert(passenger1);
            long p2Id = db.userDao().insert(passenger2);
            long p3Id = db.userDao().insert(passenger3);
            long owner1Id = db.userDao().insert(ownerUser1);
            long owner2Id = db.userDao().insert(ownerUser2);
            long driver1Id = db.userDao().insert(driverUser1);
            long driver2Id = db.userDao().insert(driverUser2);

            // 2. Create Bus Owners
            BusOwner busOwner1 = new BusOwner((int)owner1Id, "Wilson Transport", "REG123456", "TAX789012");
            BusOwner busOwner2 = new BusOwner((int)owner2Id, "Connor Express", "REG789012", "TAX345678");
            
            long busOwner1Id = db.busOwnerDao().insert(busOwner1);
            long busOwner2Id = db.busOwnerDao().insert(busOwner2);

            // 3. Create Bus Drivers and wait for successful insertion
            BusDriver driver1 = new BusDriver((int)driver1Id, "DL123456", 
                System.currentTimeMillis() + 31536000000L, 5);
            BusDriver driver2 = new BusDriver((int)driver2Id, "DL789012", 
                System.currentTimeMillis() + 63072000000L, 8);
            
            long driver1DbId = db.busDriverDao().insert(driver1);
            long driver2DbId = db.busDriverDao().insert(driver2);

            // 4. Create Locations
            Location colombo = new Location("Colombo", 6.927079, 79.861243);
            Location kandy = new Location("Kandy", 7.290572, 80.633728);
            Location galle = new Location("Galle", 6.053519, 80.220978);
            Location jaffna = new Location("Jaffna", 9.661302, 80.025513);
            Location anuradhapura = new Location("Anuradhapura", 8.311338, 80.403656);
            Location trincomalee = new Location("Trincomalee", 8.578132, 81.233040);
            Location batticaloa = new Location("Batticaloa", 7.717935, 81.700088);
            Location kurunegala = new Location("Kurunegala", 7.486842, 80.362439);
            Location matara = new Location("Matara", 5.948853, 80.535888);
            Location negombo = new Location("Negombo", 7.189464, 79.858734);
            Location ratnapura = new Location("Ratnapura", 6.693813, 80.405845);
            Location badulla = new Location("Badulla", 6.989821, 81.054276);
            Location polonnaruwa = new Location("Polonnaruwa", 7.940576, 81.018193);
            Location hambantota = new Location("Hambantota", 6.127064, 81.111197);
            Location dambulla = new Location("Dambulla", 7.868039, 80.650698);

            // Insert locations
            db.locationDao().insert(colombo);
            db.locationDao().insert(kandy);
            db.locationDao().insert(galle);
            db.locationDao().insert(jaffna);
            db.locationDao().insert(anuradhapura);
            db.locationDao().insert(trincomalee);
            db.locationDao().insert(batticaloa);
            db.locationDao().insert(kurunegala);
            db.locationDao().insert(matara);
            db.locationDao().insert(negombo);
            db.locationDao().insert(ratnapura);
            db.locationDao().insert(badulla);
            db.locationDao().insert(polonnaruwa);
            db.locationDao().insert(hambantota);
            db.locationDao().insert(dambulla);

            // 5. Now create Buses with confirmed driver IDs
            Bus bus1 = new Bus(
                (int)busOwner1Id,
                (int)driver1DbId,  // Use the actual database ID
                "NB-1234",
                "Volvo 9400",
                40,
                "WiFi, AC, USB Charging",
                true,
                "Colombo",
                "Kandy",
                colombo.getLatitude(),
                colombo.getLongitude(),
                System.currentTimeMillis() + 3600000,
                System.currentTimeMillis() + 18000000,
                2500
            );
            bus1.setRating(4.5f);
            bus1.setRatingCount(12);

            Bus bus2 = new Bus(
                (int)busOwner1Id,
                (int)driver1DbId,
                "NB-5678",
                "Volvo 9400",
                40,
                "WiFi, AC, USB Charging, Entertainment",
                true,
                "Colombo",
                "Galle",
                colombo.getLatitude(),
                colombo.getLongitude(),
                System.currentTimeMillis() + 7200000,
                System.currentTimeMillis() + 14400000,
                3500
            );
            bus2.setRating(4.2f);
            bus2.setRatingCount(8);

            Bus bus3 = new Bus(
                (int)busOwner2Id,
                (int)driver2DbId,
                "NB-9012",
                "Mercedes-Benz O303",
                45,
                "WiFi, AC, USB Charging, Entertainment, Refreshments",
                true,
                "Colombo",
                "Jaffna",
                colombo.getLatitude(),
                colombo.getLongitude(),
                System.currentTimeMillis() + 10800000,
                System.currentTimeMillis() + 28800000,
                5000
            );
            bus3.setRating(4.8f);
            bus3.setRatingCount(15);

            long bus1Id = db.busDao().insert(bus1);
            long bus2Id = db.busDao().insert(bus2);
            long bus3Id = db.busDao().insert(bus3);

            // 6. Create Tickets and Payments for various scenarios
            // Completed bookings for Passenger 1
            createTicketAndPayment(db, (int)p1Id, (int)bus1Id, 1, 2625, "completed", true);
            createTicketAndPayment(db, (int)p1Id, (int)bus2Id, 7, 2100, "booked", false);

            // Completed bookings for Passenger 2
            createTicketAndPayment(db, (int)p2Id, (int)bus2Id, 10, 2100, "completed", true);
            createTicketAndPayment(db, (int)p2Id, (int)bus3Id, 16, 3675, "cancelled", false);

            // Completed bookings for Passenger 3
            createTicketAndPayment(db, (int)p3Id, (int)bus1Id, 6, 2625, "completed", true);
            createTicketAndPayment(db, (int)p3Id, (int)bus3Id, 3, 3675, "booked", false);

            // 7. Create Notifications for various scenarios
            createNotifications(db, (int)driver1Id, (int)driver2Id, (int)p1Id, (int)p2Id, (int)p3Id);
        });
    }

    private static void createTicketAndPayment(AppDatabase db, int userId, int busId, 
        int seatNumber, int points, String status, boolean isRated) {
        
        Ticket ticket = new Ticket(userId, busId, seatNumber,
            System.currentTimeMillis() + 86400000, // Journey tomorrow
            "Colombo", "Kandy", status);
        ticket.setRated(isRated);
        long ticketId = db.ticketDao().insert(ticket);

        Payment payment = new Payment(0, userId, (int)ticketId, points);
        long paymentId = db.paymentDao().insert(payment);

        db.ticketDao().updateTicketPaymentId((int)ticketId, (int)paymentId);
        db.userDao().deductPoints(userId, points);
    }

    private static void createNotifications(AppDatabase db, int driver1Id, int driver2Id, 
        int passenger1Id, int passenger2Id, int passenger3Id) {
        // Method left empty as all test notifications are removed
    }
}
