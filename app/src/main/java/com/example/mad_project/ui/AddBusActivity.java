package com.example.mad_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.LocationController;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.Location;
import com.example.mad_project.data.Notification;
import com.example.mad_project.data.User;
import com.example.mad_project.utils.NotificationHandler;
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
        setContentView(R.layout.activity_add_bus);

        db = AppDatabase.getDatabase(this);
        initializeViews();
        
        boolean isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);
        
        if (isEditMode) {
            setTitle("Edit Bus");
            addBusButton.setText("Update Bus");
            
            // Populate fields with existing bus data
            registrationInput.setText(getIntent().getStringExtra("BUS_REGISTRATION"));
            modelInput.setText(getIntent().getStringExtra("BUS_MODEL"));
            seatsInput.setText(String.valueOf(getIntent().getIntExtra("BUS_SEATS", 0)));
            fromLocationInput.setText(getIntent().getStringExtra("BUS_FROM"));
            toLocationInput.setText(getIntent().getStringExtra("BUS_TO"));
            basePointsInput.setText(String.valueOf(getIntent().getIntExtra("BUS_BASE_POINTS", 0)));
            
            // Disable registration number editing
            registrationInput.setEnabled(false);
        } else {
            setTitle("Add New Bus");
            addBusButton.setText("Add Bus");
        }

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
        LocationController locationController = new LocationController(this);
        locationController.getAllLocations(locations -> {
            runOnUiThread(() -> {
                ArrayAdapter<Location> adapter = new ArrayAdapter<>(
                    this, 
                    android.R.layout.simple_dropdown_item_1line, 
                    locations
                );
                fromLocationInput.setAdapter(adapter);
                toLocationInput.setAdapter(adapter);
            });
        });
    }

    private void setupAddBusButton() {
        addBusButton = findViewById(R.id.addBusButton);
        addBusButton.setOnClickListener(v -> {
            executor.execute(() -> {
                if (validateInputs()) {
                    addBus();
                }
            });
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;
        
        if (registrationInput.getText().toString().trim().isEmpty()) {
            registrationInput.setError("Registration number is required");
            isValid = false;
        }
        
        if (modelInput.getText().toString().trim().isEmpty()) {
            modelInput.setError("Model is required");
            isValid = false;
        }
        
        try {
            int seats = Integer.parseInt(seatsInput.getText().toString().trim());
            if (seats <= 0) {
                seatsInput.setError("Number of seats must be greater than 0");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            seatsInput.setError("Invalid number of seats");
            isValid = false;
        }
        
        if (fromLocationInput.getText().toString().trim().isEmpty()) {
            fromLocationInput.setError("From location is required");
            isValid = false;
        }
        
        if (toLocationInput.getText().toString().trim().isEmpty()) {
            toLocationInput.setError("To location is required");
            isValid = false;
        }
        
        try {
            int points = Integer.parseInt(basePointsInput.getText().toString().trim());
            if (points <= 0) {
                basePointsInput.setError("Base points must be greater than 0");
                isValid = false;
            }
        } catch (NumberFormatException e) {
            basePointsInput.setError("Invalid base points");
            isValid = false;
        }
        
        return isValid;
    }

    private void addBus() {
        SessionManager sessionManager = new SessionManager(this);
        int userId = sessionManager.getUserId();
        boolean isEditMode = getIntent().getBooleanExtra("EDIT_MODE", false);

        // Get selected driver
        int selectedPosition = getSelectedDriverPosition();
        if (selectedPosition == -1) {
            runOnUiThread(() -> {
                Toast.makeText(this, "Please select a driver", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        BusDriver selectedDriver = availableDrivers.get(selectedPosition);

        try {
            BusOwner busOwner = db.busOwnerDao().getBusOwnerByUserId(userId);
            
            if (busOwner == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "You are not registered as a bus owner", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Check registration only in add mode
            if (!isEditMode) {
                Bus existingBus = db.busDao().getBusByRegistration(
                    registrationInput.getText().toString().trim()
                );
                
                if (existingBus != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Bus registration number already exists", Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
            }

            Location fromLocation = db.locationDao().getLocationByName(
                fromLocationInput.getText().toString().trim()
            );
            Location toLocation = db.locationDao().getLocationByName(
                toLocationInput.getText().toString().trim()
            );

            if (fromLocation == null || toLocation == null) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Invalid locations selected", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            if (isEditMode) {
                // Update existing bus
                int busId = getIntent().getIntExtra("BUS_ID", -1);
                Bus bus = db.busDao().getBusById(busId);
                if (bus != null) {
                    bus.setModel(modelInput.getText().toString().trim());
                    bus.setTotalSeats(Integer.parseInt(seatsInput.getText().toString().trim()));
                    bus.setDriverId(selectedDriver.getId());
                    bus.setRouteFrom(fromLocation.getName());
                    bus.setRouteTo(toLocation.getName());
                    bus.setLatitude(fromLocation.getLatitude());
                    bus.setLongitude(fromLocation.getLongitude());
                    bus.setBasePoints(Integer.parseInt(basePointsInput.getText().toString().trim()));
                    
                    db.busDao().update(bus);
                    
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Bus updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }
            } else {
                // Create new bus
                Bus bus = new Bus(
                    busOwner.getId(),
                    selectedDriver.getId(),
                    registrationInput.getText().toString().trim(),
                    modelInput.getText().toString().trim(),
                    Integer.parseInt(seatsInput.getText().toString().trim()),
                    "WiFi, AC",
                    true,
                    fromLocation.getName(),
                    toLocation.getName(),
                    fromLocation.getLatitude(),
                    fromLocation.getLongitude(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis() + 3600000,
                    Integer.parseInt(basePointsInput.getText().toString().trim())
                );
                db.busDao().insert(bus);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Bus added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }

        } catch (Exception e) {
            Log.e("AddBusActivity", "Error " + (isEditMode ? "updating" : "adding") + " bus", e);
            runOnUiThread(() -> {
                Toast.makeText(this, "Failed to " + (isEditMode ? "update" : "add") + " bus: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
        }
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

    private void createNotifications(BusDriver driver, Bus bus, BusOwner owner) {
        NotificationHandler notificationHandler = new NotificationHandler(this);
        notificationHandler.createBusAssignmentNotification(driver, bus, owner);
    }
} 