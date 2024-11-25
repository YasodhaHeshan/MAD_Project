package com.example.mad_project.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.data.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.mad_project.data.BusDriver;
import com.example.mad_project.data.BusDriverDao;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.utils.SessionManager;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterDriverActivity extends MainActivity {

    private TextInputEditText licenseNumberInput;
    private TextInputEditText experienceInput;
    private TextInputEditText expiryDateInput;
    private MaterialButton registerButton;
    private Calendar calendar;
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register_driver, contentFrame);
        setupNavigation(true, false, "Register as Driver");

        // Initialize views and setup listeners
        licenseNumberInput = findViewById(R.id.licenseNumberInput);
        experienceInput = findViewById(R.id.experienceInput);
        expiryDateInput = findViewById(R.id.expiryDateInput);
        registerButton = findViewById(R.id.registerButton);
        calendar = Calendar.getInstance();

        expiryDateInput.setOnClickListener(v -> showDatePicker());
        registerButton.setOnClickListener(v -> attemptRegistration());
    }

    private void showDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
            this,
            (view, year, month, day) -> {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel();
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateLabel() {
        String format = "MM/dd/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        expiryDateInput.setText(dateFormat.format(calendar.getTime()));
    }

    private void attemptRegistration() {
        // Get current user ID
        int userId = SessionManager.getInstance(this).getUserId();
        
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(this);
                
                // Check if user already registered as driver
                BusDriver existingDriver = db.busDriverDao().getDriverByUserId(userId);
                if (existingDriver != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "You are already registered as a driver", 
                            Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                
                // Continue with existing registration logic
                String licenseNumber = licenseNumberInput.getText().toString().trim();
                String experience = experienceInput.getText().toString().trim();
                String expiryDate = expiryDateInput.getText().toString().trim();
                
                // Validate inputs
                if (!validateInputs(licenseNumber, experience, expiryDate)) {
                    return;
                }
                
                // Create BusDriver object
                BusDriver driver = new BusDriver(
                    userId,
                    licenseNumber,
                    calendar.getTimeInMillis(),
                    Integer.parseInt(experience)
                );
                
                // Save to database asynchronously
                BusDriverDao driverDao = db.busDriverDao();
                
                // Check if license number already exists
                if (driverDao.isLicenseExists(licenseNumber)) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "License number already registered", 
                            Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                
                driverDao.insert(driver);
                
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                    finish();
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration failed: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                    Log.e("RegisterDriverActivity", "Registration failed", e);
                });
            }
        });
    }

    private boolean validateInputs(String licenseNumber, String experience, String expiryDate) {
        boolean isValid = true;
        
        // Validate license number
        if (licenseNumber.isEmpty()) {
            licenseNumberInput.setError("License number is required");
            isValid = false;
        } else if (licenseNumber.length() < 5) {
            licenseNumberInput.setError("Invalid license number format");
            isValid = false;
        }
        
        // Validate experience
        if (experience.isEmpty()) {
            experienceInput.setError("Experience is required");
            isValid = false;
        } else {
            try {
                int exp = Integer.parseInt(experience);
                if (exp < 0 || exp > 50) {
                    experienceInput.setError("Invalid years of experience");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                experienceInput.setError("Invalid number");
                isValid = false;
            }
        }
        
        // Validate expiry date
        if (expiryDate.isEmpty()) {
            expiryDateInput.setError("Expiry date is required");
            isValid = false;
        } else if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            expiryDateInput.setError("License must not be expired");
            isValid = false;
        }
        
        return isValid;
    }
} 