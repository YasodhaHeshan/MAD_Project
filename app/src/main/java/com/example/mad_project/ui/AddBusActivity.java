package com.example.mad_project.ui;

import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.User;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddBusActivity extends MainActivity {
    private TextInputEditText registrationInput;
    private TextInputEditText modelInput;
    private TextInputEditText seatsInput;
    private AutoCompleteTextView driverInput;
    private AutoCompleteTextView fromLocationInput;
    private AutoCompleteTextView toLocationInput;
    private TextInputEditText basePointsInput;
    private MaterialButton addBusButton;
    private AppDatabase db;
    private Executor executor = Executors.newSingleThreadExecutor();
    private List<BusDriver> availableDrivers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_add_bus, contentFrame);
        setupNavigation(true, false, "Add New Bus");

        db = AppDatabase.getDatabase(this);
        initializeViews();
        loadDrivers();
        setupLocationAdapters();
        setupAddBusButton();
    }

    private void initializeViews() {
        registrationInput = findViewById(R.id.registrationInput);
        modelInput = findViewById(R.id.modelInput);
        seatsInput = findViewById(R.id.seatsInput);
        driverInput = findViewById(R.id.driverInput);
        fromLocationInput = findViewById(R.id.fromLocationInput);
        toLocationInput = findViewById(R.id.toLocationInput);
        basePointsInput = findViewById(R.id.basePointsInput);
        addBusButton = findViewById(R.id.addBusButton);
    }

    private void loadDrivers() {
        executor.execute(() -> {
            List<BusDriver> drivers = db.busDriverDao().getAllActiveDrivers();
            availableDrivers.clear();
            availableDrivers.addAll(drivers);

            List<String> driverNames = new ArrayList<>();
            for (BusDriver driver : drivers) {
                User user = db.userDao().getUserById(driver.getUserId());
                if (user != null) {
                    driverNames.add(user.getName() + " (" + driver.getLicenseNumber() + ")");
                }
            }

            runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this, android.R.layout.simple_dropdown_item_1line, driverNames
                );
                driverInput.setAdapter(adapter);
            });
        });
    }

    private void setupLocationAdapters() {
        String[] locations = getResources().getStringArray(R.array.locations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this, android.R.layout.simple_dropdown_item_1line, locations
        );
        fromLocationInput.setAdapter(adapter);
        toLocationInput.setAdapter(adapter);
    }

    private void setupAddBusButton() {
        addBusButton.setOnClickListener(v -> {
            if (validateInputs()) {
                addBus();
            }
        });
    }

    private boolean validateInputs() {
        // Add validation logic here
        return true;
    }

    private void addBus() {
        SessionManager sessionManager = new SessionManager(this);
        int ownerId = sessionManager.getUserId();

        // Get selected driver
        int selectedPosition = getSelectedDriverPosition();
        if (selectedPosition == -1) {
            Toast.makeText(this, "Please select a driver", Toast.LENGTH_SHORT).show();
            return;
        }

        BusDriver selectedDriver = availableDrivers.get(selectedPosition);

        Bus newBus = new Bus(
            ownerId,
            registrationInput.getText().toString(),
            modelInput.getText().toString(),
            Integer.parseInt(seatsInput.getText().toString()),
            "WiFi, AC", // Default amenities
            true,
            fromLocationInput.getText().toString(),
            toLocationInput.getText().toString(),
            0.0, 0.0, // Default coordinates
            System.currentTimeMillis(),
            System.currentTimeMillis() + 3600000,
            Integer.parseInt(basePointsInput.getText().toString())
        );

        executor.execute(() -> {
            try {
                long busId = db.busDao().insert(newBus);
                // Create notification for the selected driver
                createDriverAssignmentNotification(selectedDriver.getUserId(), newBus);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Bus added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Failed to add bus", Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private int getSelectedDriverPosition() {
        String selectedDriver = driverInput.getText().toString();
        List<String> driverNames = new ArrayList<>();
        for (BusDriver driver : availableDrivers) {
            User user = db.userDao().getUserById(driver.getUserId());
            if (user != null) {
                String driverName = user.getName() + " (" + driver.getLicenseNumber() + ")";
                if (driverName.equals(selectedDriver)) {
                    return availableDrivers.indexOf(driver);
                }
            }
        }
        return -1;
    }

    private void createDriverAssignmentNotification(int driverId, Bus bus) {
        // Create notification for driver assignment
        // Implementation referenced from NotificationController.java
    }
} 