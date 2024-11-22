package com.example.mad_project.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mad_project.R;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDao;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.data.Payment;
import com.example.mad_project.data.PaymentDao;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.TicketDao;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.utils.HashPassword;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FillDatabaseActivity extends AppCompatActivity {
    private AppDatabase db;
    private ProgressBar progressBar;
    private TextView statusText;
    private Button exitButton;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        initializeViews();
        setupDatabase();
    }

    private void initializeViews() {
        progressBar = findViewById(R.id.progressBar);
        statusText = findViewById(R.id.statusText);
        exitButton = findViewById(R.id.exitButton);
        
        progressBar.setMax(7); // Total number of operations
        progressBar.setProgress(0);
        
        exitButton.setOnClickListener(v -> finish());
    }

    private void setupDatabase() {
        try {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "mad_project_db")
                    .fallbackToDestructiveMigration()
                    .build();
            updateProgress("Database setup successful");
            showRebuildDatabaseDialog();
        } catch (IllegalStateException e) {
            showError("Database setup failed: " + e.getMessage());
        }
    }

    private void showRebuildDatabaseDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Database Schema Changed")
                .setMessage("The database schema has changed. Would you like to rebuild the database? This will delete all existing data.")
                .setPositiveButton("Rebuild", (dialog, which) -> {
                    rebuildDatabase();
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    private void rebuildDatabase() {
        executor.execute(() -> {
            try {
                // Delete the existing database
                getApplicationContext().deleteDatabase("mad_project_db");
                updateProgress("Old database deleted");

                // Create new database instance
                db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "mad_project_db")
                        .fallbackToDestructiveMigration()
                        .build();
                updateProgress("New database created");

                // Start filling the database
                clearDatabase();
                startDatabaseFill();
            } catch (Exception e) {
                Log.e("FillDatabaseActivity", "Error rebuilding database", e);
                showError("Failed to rebuild database: " + e.getMessage());
            }
        });
    }

    private void clearDatabase() {
        executor.execute(() -> {
            db.clearAllTables();
            updateProgress("Database cleared");
        });
    }


    private void startDatabaseFill() {
        executor.execute(() -> {
            try {
                updateProgress("Starting database fill");

                createUsers();
                Thread.sleep(100);
                
                createBusOwners();
                Thread.sleep(100);
                
                createBusDrivers();
                Thread.sleep(100);
                
                createBuses();
                Thread.sleep(100);
                
                createTickets();
                Thread.sleep(100);
                
                createPayments();
                Thread.sleep(100);
                
                updateTicketsWithPayments();
                Thread.sleep(100);

                updateProgress("Database fill completed");
            } catch (Exception e) {
                Log.e("FillDatabaseActivity", "Error filling database", e);
                showError("Error: " + e.getMessage());
            }
        });
    }

    private void updateProgress(String status) {
        runOnUiThread(() -> {
            statusText.setText(status);
            progressBar.setProgress(progressBar.getProgress() + 1);
        });
    }

    private void showError(String message) {
        runOnUiThread(() -> {
            statusText.setText("Error: " + message);
            statusText.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            exitButton.setText(R.string.close_text);
        });
    }

    private void createUsers() throws NoSuchAlgorithmException {
        UserDao userDao = db.userDao();
        String hashedPassword = HashPassword.hashPassword("a");

        User[] users = {
            // Regular users
            new User("John Doe", "john@mail.com", "1234567890", hashedPassword, "user"),
            new User("Jane Smith", "a", "0987654321", hashedPassword, "user"),
            // Driver users
            new User("Driver One", "driver1@mail.com", "1111111111", hashedPassword, "driver"),
            new User("Driver Two", "driver2@mail.com", "2222222222", hashedPassword, "driver")
        };

        for (User user : users) {
            userDao.insert(user);
            updateProgress("Created user: " + user.getEmail());
        }
    }

    private void createBuses() {
        BusDao busDao = db.busDao();

        // Get current time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6); // Start at 6 AM
        calendar.set(Calendar.MINUTE, 0);
        long baseTime = calendar.getTimeInMillis();

        Bus[] buses = {
            // Colombo - Kandy Route (A1 Highway)
            new Bus(1, "NA-1234", "Volvo B11R", 45, "WiFi, AC, USB Charging, Reclining Seats", true, 
                "Colombo", "Kandy", 6.9271, 79.8612, 
                baseTime, baseTime + (3 * 3600000)), // 6:00 AM - 9:00 AM

            new Bus(1, "NC-5678", "Yutong ZK6147H", 53, "WiFi, AC, Entertainment System", true, 
                "Kandy", "Colombo", 7.2906, 80.6337,
                baseTime + (4 * 3600000), baseTime + (7 * 3600000)), // 10:00 AM - 1:00 PM

            // Colombo - Galle Route (Southern Expressway)
            new Bus(2, "NB-9012", "King Long XMQ6129Y", 48, "WiFi, AC, LCD TV", true, 
                "Colombo", "Galle", 6.9271, 79.8612,
                baseTime + (8 * 3600000), baseTime + (10 * 3600000)), // 2:00 PM - 4:00 PM

            new Bus(2, "ND-3456", "Volvo B8R", 42, "WiFi, AC, Luxury Seats", true, 
                "Galle", "Colombo", 6.0535, 80.2210,
                baseTime + (11 * 3600000), baseTime + (13 * 3600000)) // 5:00 PM - 7:00 PM
        };

        for (Bus bus : buses) {
            busDao.insert(bus);
            updateProgress("Created bus: " + bus.getRegistrationNumber());
        }
    }

    private void createBusOwners() {
        BusOwnerDao busOwnerDao = db.busOwnerDao();

        BusOwner[] busOwners = {
            new BusOwner(1, "Lanka Express", "REG001", "TAX001"),
            new BusOwner(2, "Southern Travels", "REG002", "TAX002"),
            new BusOwner(3, "Highway Links", "REG003", "TAX003")
        };

        for (BusOwner busOwner : busOwners) {
            busOwnerDao.insert(busOwner);
            updateProgress("Created bus owner: " + busOwner.getCompanyName());
        }
    }

    private void createBusDrivers() {
        BusDriverDao busDriverDao = db.busDriverDao();

        BusDriver[] busDrivers = {
            new BusDriver(3, "DL123", System.currentTimeMillis() + 31536000000L, 5),  // user_id 3
            new BusDriver(4, "DL456", System.currentTimeMillis() + 31536000000L, 10)  // user_id 4
        };

        for (BusDriver busDriver : busDrivers) {
            busDriverDao.insert(busDriver);
            updateProgress("Created bus driver: " + busDriver.getLicenseNumber());
        }
    }

    private void createTickets() {
        TicketDao ticketDao = db.ticketDao();

        long journeyDate = System.currentTimeMillis();

        Ticket[] tickets = {
            new Ticket(1, 1, "A1", journeyDate, "Colombo", "Galle", "booked"),
            new Ticket(2, 2, "A2", journeyDate, "Colombo", "Katunayaka", "booked")
        };

        for (Ticket ticket : tickets) {
            ticketDao.insert(ticket);
            updateProgress("Created ticket: " + ticket.getId());
        }
    }

    private void createPayments() {
        PaymentDao paymentDao = db.paymentDao();

        Payment[] payments = {
            new Payment(1, 1, 100.0, "Cash"),
            new Payment(2, 2, 200.0, "Card")
        };

        for (Payment payment : payments) {
            paymentDao.insert(payment);
            updateProgress("Created payment: " + payment.getId());
        }
    }

    private void updateTicketsWithPayments() {
        TicketDao ticketDao = db.ticketDao();
        
        ticketDao.updateTicketPaymentId(1, 1);
        ticketDao.updateTicketPaymentId(2, 2);
        updateProgress("Updated tickets with payment IDs");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            executor.execute(() -> db.close());
        }
    }
}