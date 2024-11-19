package com.example.mad_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import com.example.mad_project.data.Route;
import com.example.mad_project.data.RouteDao;
import com.example.mad_project.data.Schedule;
import com.example.mad_project.data.ScheduleDao;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.TicketDao;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.utils.HashPassword;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class FillDatabaseActivity extends AppCompatActivity {
    private AppDatabase db;
    private ListView progressListView;
    private Button exitButton;
    private ArrayAdapter<String> progressAdapter;
    private List<String> progressMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_test);

        initializeViews();
        setupDatabase();
        startDatabaseFill();
    }

    private void initializeViews() {
        progressListView = findViewById(R.id.progressListView);
        exitButton = findViewById(R.id.exitButton);

        progressMessages = new ArrayList<>();
        progressAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, progressMessages);
        progressListView.setAdapter(progressAdapter);

        exitButton.setOnClickListener(v -> finish());
    }

    private void setupDatabase() {
        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class,
                        "mad_project_db")
                .fallbackToDestructiveMigration()
                .build();
    }

    private void startDatabaseFill() {
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                updateProgress("Starting database fill");

                createUsers();
                createBusOwners();
                createBusDrivers();
                createBuses();
                createRoutes();
                createTickets();
                createPayments();
                createSchedules();

                updateProgress("Database fill completed");
            } catch (Exception e) {
                Log.e("FillDatabaseActivity", "Error filling database", e);
                showError("Error: " + e.getMessage());
            }
        });
    }

    private synchronized void updateProgress(String status) {
        runOnUiThread(() -> {
            progressMessages.add(status);
            progressAdapter.notifyDataSetChanged();
            progressListView.smoothScrollToPosition(progressMessages.size() - 1);
        });
    }

    private void showError(String message) {
        runOnUiThread(() -> {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            progressMessages.add("Error: " + message);
            progressAdapter.notifyDataSetChanged();
            exitButton.setText(R.string.close_text);
        });
    }

    private void createUsers() {
        try {
            UserDao userDao = db.userDao();
            User[] users = {
                    new User("John", "Doe", "john@mail.com", "1234567890",
                            HashPassword.hashPassword("john@mail.com", "a")),
                    new User("Jane", "Smith", "a", "0987654321",
                            HashPassword.hashPassword("a", "a"))
            };

            for (User user : users) {
                userDao.insert(user);
                updateProgress("Created user: " + user.getEmail());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating users: " + e.getMessage());
        }
    }

    private void createBuses() {
        try {
            BusDao busDao = db.busDao();
            Bus[] buses = {
                    new Bus("BUS123", "Colombo", "Galle", 40),
                    new Bus("BUS456", "Colombo", "Katunayaka", 50)
            };

            for (Bus bus : buses) {
                busDao.insert(bus);
                updateProgress("Created bus: " + bus.getBusNumber());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating buses: " + e.getMessage());
        }
    }

    private void createBusOwners() {
        try {
            BusOwnerDao busOwnerDao = db.busOwnerDao();
            BusOwner[] busOwners = {
                    new BusOwner(1, "Bus Company A", "BUS123", "1234567890"),
                    new BusOwner(2, "Bus Company B", "BUS456", "0987654321")
            };

            for (BusOwner busOwner : busOwners) {
                busOwnerDao.insert(busOwner);
                updateProgress("Created bus owner: " + busOwner.getCompanyName());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating bus owners: " + e.getMessage());
        }
    }

    private void createBusDrivers() {
        try {
            BusDriverDao busDriverDao = db.busDriverDao();
            BusDriver[] busDrivers = {
                    new BusDriver(1, "DL123", "1234567890"),
                    new BusDriver(2, "DL456", "0987654321")
            };

            for (BusDriver busDriver : busDrivers) {
                busDriverDao.insert(busDriver);
                updateProgress("Created bus driver: " + busDriver.getLicenseNumber());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating bus drivers: " + e.getMessage());
        }
    }

    private void createRoutes() {
        try {
            RouteDao routeDao = db.routeDao();
            Route[] routes = {
                    new Route(1, "Location A", "Location B", "10.00AM", "12.00PM"),
                    new Route(2, "Location C", "Location D", "15.00PM", "17.00PM")
            };

            for (Route route : routes) {
                routeDao.insert(route);
                updateProgress("Created route: " + route.getStartLocation() + " to " + route.getEndLocation());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating routes: " + e.getMessage());
        }
    }

    private void createTickets() {
        try {
            TicketDao ticketDao = db.ticketDao();
            Ticket[] tickets = {
                    new Ticket(1, 1, 1, 1, 1000.00, "2021-12-01", "2022-01-01"),
                    new Ticket(2, 2, 2, 2, 1000.00, "2021-12-02", "2022-01-02")
            };

            for (Ticket ticket : tickets) {
                ticketDao.insert(ticket);
                updateProgress("Created ticket: " + ticket.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating tickets: " + e.getMessage());
        }
    }

    private void createPayments() {
        try {
            PaymentDao paymentDao = db.paymentDao();
            Payment[] payments = {
                    new Payment(1, 1, "2021-12-01", "Cash"),
                    new Payment(2, 2, "2021-12-02", "Card")
            };

            for (Payment payment : payments) {
                paymentDao.insert(payment);
                updateProgress("Created payment: " + payment.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating payments: " + e.getMessage());
        }
    }

    private void createSchedules() {
        try {
            ScheduleDao scheduleDao = db.scheduleDao();
            Schedule[] schedules = {
                    new Schedule(1, 1, "08:00", "10:00"),
                    new Schedule(2, 2, "12:00", "14:00")
            };

            for (Schedule schedule : schedules) {
                scheduleDao.insert(schedule);
                updateProgress("Created schedule: " + schedule.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating schedules: " + e.getMessage());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }
}