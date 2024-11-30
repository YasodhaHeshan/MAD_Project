package com.example.mad_project.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.mad_project.R;
import com.example.mad_project.controller.LocationController;
import com.example.mad_project.data.*;
import com.example.mad_project.ui.ManageBusesActivity;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddBusFragment extends Fragment {
    private TextInputEditText registrationInput;
    private TextInputEditText modelInput;
    private TextInputEditText seatsInput;
    private AutoCompleteTextView driverInput;
    private AutoCompleteTextView fromLocationInput;
    private AutoCompleteTextView toLocationInput;
    private TextInputEditText basePointsInput;
    private MaterialButton addBusButton;
    private AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private List<BusDriver> availableDrivers = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_bus, container, false);
        
        db = AppDatabase.getDatabase(requireContext());
        initializeViews(view);
        loadDrivers();
        setupLocationAdapters();
        setupAddBusButton();
        
        return view;
    }

    private void initializeViews(View view) {
        registrationInput = view.findViewById(R.id.registrationInput);
        modelInput = view.findViewById(R.id.modelInput);
        seatsInput = view.findViewById(R.id.seatsInput);
        driverInput = view.findViewById(R.id.driverInput);
        fromLocationInput = view.findViewById(R.id.fromLocationInput);
        toLocationInput = view.findViewById(R.id.toLocationInput);
        basePointsInput = view.findViewById(R.id.basePointsInput);
        addBusButton = view.findViewById(R.id.addBusButton);
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

            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    requireContext(), 
                    android.R.layout.simple_dropdown_item_1line, 
                    driverNames
                );
                driverInput.setAdapter(adapter);
            });
        });
    }

    private void setupLocationAdapters() {
        LocationController locationController = new LocationController(requireContext());
        locationController.getAllLocations(locations -> {
            requireActivity().runOnUiThread(() -> {
                ArrayAdapter<Location> adapter = new ArrayAdapter<>(
                    requireContext(), 
                    android.R.layout.simple_dropdown_item_1line, 
                    locations
                );
                fromLocationInput.setAdapter(adapter);
                toLocationInput.setAdapter(adapter);
            });
        });
    }

    private void setupAddBusButton() {
        addBusButton.setOnClickListener(v -> {
            executor.execute(() -> {
                if (validateInputs()) {
                    addBus();
                }
            });
        });
    }

    private boolean validateInputs() {
        String registration = registrationInput.getText().toString().trim();
        String model = modelInput.getText().toString().trim();
        String seats = seatsInput.getText().toString().trim();
        String driver = driverInput.getText().toString().trim();
        String from = fromLocationInput.getText().toString().trim();
        String to = toLocationInput.getText().toString().trim();
        String basePoints = basePointsInput.getText().toString().trim();

        if (registration.isEmpty() || model.isEmpty() || seats.isEmpty() || 
            driver.isEmpty() || from.isEmpty() || to.isEmpty() || basePoints.isEmpty()) {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            });
            return false;
        }

        try {
            Integer.parseInt(seats);
            Integer.parseInt(basePoints);
        } catch (NumberFormatException e) {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Invalid number format", Toast.LENGTH_SHORT).show();
            });
            return false;
        }

        return true;
    }

    private int getSelectedDriverPosition() {
        String selectedText = driverInput.getText().toString();
        for (int i = 0; i < availableDrivers.size(); i++) {
            BusDriver driver = availableDrivers.get(i);
            User user = db.userDao().getUserById(driver.getUserId());
            String driverText = user.getName() + " (" + driver.getLicenseNumber() + ")";
            if (driverText.equals(selectedText)) {
                return i;
            }
        }
        return -1;
    }

    private void addBus() {
        SessionManager sessionManager = new SessionManager(requireContext());
        int userId = sessionManager.getUserId();

        // Get selected driver
        int selectedPosition = getSelectedDriverPosition();
        if (selectedPosition == -1) {
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), "Please select a driver", Toast.LENGTH_SHORT).show();
            });
            return;
        }

        BusDriver selectedDriver = availableDrivers.get(selectedPosition);

        try {
            // First get the bus owner ID using user ID
            BusOwner busOwner = db.busOwnerDao().getBusOwnerByUserId(userId);
            
            if (busOwner == null) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "You are not registered as a bus owner", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Check if registration number is unique
            Bus existingBus = db.busDao().getBusByRegistration(
                registrationInput.getText().toString().trim()
            );
            
            if (existingBus != null) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Registration number already exists", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Get location objects
            Location fromLocation = db.locationDao().getLocationByName(
                fromLocationInput.getText().toString().trim()
            );
            Location toLocation = db.locationDao().getLocationByName(
                toLocationInput.getText().toString().trim()
            );

            if (fromLocation == null || toLocation == null) {
                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Invalid locations selected", Toast.LENGTH_SHORT).show();
                });
                return;
            }

            // Create new bus with proper owner ID but set isActive to false initially
            Bus newBus = new Bus(
                busOwner.getId(),
                selectedDriver.getId(),
                registrationInput.getText().toString().trim(),
                modelInput.getText().toString().trim(),
                Integer.parseInt(seatsInput.getText().toString().trim()),
                "WiFi, AC",
                true,  // Set to active
                fromLocation.getName(),
                toLocation.getName(),
                fromLocation.getLatitude(),
                fromLocation.getLongitude(),
                System.currentTimeMillis(),
                System.currentTimeMillis() + 3600000,
                Integer.parseInt(basePointsInput.getText().toString().trim())
            );

            // Use transaction to ensure data consistency
            db.runInTransaction(() -> {
                // Insert the bus first
                long busId = db.busDao().insert(newBus);
                
                // Create notifications for both driver and owner
                createDriverNotification(selectedDriver, newBus, busOwner);
                createOwnerNotification(busOwner.getUserId(), selectedDriver, newBus);
            });

            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), 
                    "Bus assignment request sent to driver", Toast.LENGTH_SHORT).show();
                clearInputs();
                ((ManageBusesActivity) requireActivity()).refreshMyBuses();
            });

        } catch (Exception e) {
            Log.e("AddBusFragment", "Error adding bus", e);
            requireActivity().runOnUiThread(() -> {
                Toast.makeText(requireContext(), 
                    "Failed to add bus: " + e.getMessage(), 
                    Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void createDriverNotification(BusDriver driver, Bus bus, BusOwner owner) {
        Notification notification = new Notification(
            driver.getUserId(),
            "BUS_ASSIGNMENT",
            "Bus Assignment Request",
            String.format("Owner %s wants to assign you to bus %s on route %s to %s", 
                owner.getCompanyName(), 
                bus.getRegistrationNumber(),
                bus.getRouteFrom(),
                bus.getRouteTo())
        );
        notification.setAdditionalData(bus.getRegistrationNumber());
        notification.setStatus("PENDING");
        db.notificationDao().insert(notification);
    }

    private void createOwnerNotification(int ownerId, BusDriver driver, Bus bus) {
        Notification notification = new Notification(
            ownerId,
            "BUS_ASSIGNMENT_PENDING",
            "Pending Driver Acceptance",
            String.format("Waiting for driver %s to accept assignment to bus %s",
                driver.getLicenseNumber(),
                bus.getRegistrationNumber())
        );
        notification.setAdditionalData(bus.getRegistrationNumber());
        notification.setStatus("PENDING");
        db.notificationDao().insert(notification);
    }

    private void clearInputs() {
        registrationInput.setText("");
        modelInput.setText("");
        seatsInput.setText("");
        driverInput.setText("");
        fromLocationInput.setText("");
        toLocationInput.setText("");
        basePointsInput.setText("");
    }
} 