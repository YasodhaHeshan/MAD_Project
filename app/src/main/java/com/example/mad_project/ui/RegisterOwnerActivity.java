package com.example.mad_project.ui;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.example.mad_project.data.BusOwner;
import com.example.mad_project.data.BusOwnerDao;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.utils.SessionManager;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterOwnerActivity extends MainActivity {

    private TextInputEditText companyNameInput;
    private TextInputEditText registrationNumberInput;
    private TextInputEditText taxIdInput;
    private MaterialButton registerButton;

    private final Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_register_owner, contentFrame);
        setupNavigation(true, false, "Register as Owner");

        // Initialize views and setup listeners
        companyNameInput = findViewById(R.id.companyNameInput);
        registrationNumberInput = findViewById(R.id.registrationNumberInput);
        taxIdInput = findViewById(R.id.taxIdInput);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        // Get current user ID
        int userId = SessionManager.getInstance(this).getUserId();
        
        executor.execute(() -> {
            try {
                AppDatabase db = AppDatabase.getDatabase(this);
                
                // Check if user already registered as owner
                BusOwner existingOwner = db.busOwnerDao().getBusOwnerByUserId(userId);
                if (existingOwner != null) {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "You are already registered as a bus owner", 
                            Toast.LENGTH_SHORT).show();
                    });
                    return;
                }
                
                // Continue with existing registration logic
                String companyName = companyNameInput.getText().toString().trim();
                String registrationNumber = registrationNumberInput.getText().toString().trim();
                String taxId = taxIdInput.getText().toString().trim();
                
                // Validate inputs
                if (!validateInputs(companyName, registrationNumber, taxId)) {
                    return;
                }
                
                // Create BusOwner object
                BusOwner owner = new BusOwner(
                    SessionManager.getInstance(this).getUserId(),
                    companyName,
                    registrationNumber,
                    taxId
                );
                
                // Save to database asynchronously
                executor.execute(() -> {
                    try {
                        BusOwnerDao ownerDao = db.busOwnerDao();
                        
                        // Check if registration number already exists
                        if (ownerDao.isRegistrationExists(registrationNumber)) {
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Registration number already exists", 
                                    Toast.LENGTH_SHORT).show();
                            });
                            return;
                        }
                        
                        ownerDao.insert(owner);
                        
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                            finish();
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> {
                            Toast.makeText(this, "Registration failed: " + e.getMessage(), 
                                Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } catch (Exception e) {
                runOnUiThread(() -> {
                    Toast.makeText(this, "Registration failed: " + e.getMessage(), 
                        Toast.LENGTH_SHORT).show();
                    Log.e("RegisterOwnerActivity", "Registration failed", e);
                });
            }
        });
    }

    private boolean validateInputs(String companyName, String registrationNumber, String taxId) {
        boolean isValid = true;
        
        // Validate company name
        if (companyName.isEmpty()) {
            companyNameInput.setError("Company name is required");
            isValid = false;
        } else if (companyName.length() < 3) {
            companyNameInput.setError("Company name too short");
            isValid = false;
        }
        
        // Validate registration number
        if (registrationNumber.isEmpty()) {
            registrationNumberInput.setError("Registration number is required");
            isValid = false;
        } else if (!registrationNumber.matches("^REG\\d{3,}$")) {
            registrationNumberInput.setError("Invalid format (should be REGxxx)");
            isValid = false;
        }
        
        // Validate tax ID
        if (taxId.isEmpty()) {
            taxIdInput.setError("Tax ID is required");
            isValid = false;
        } else if (!taxId.matches("^TAX\\d{3,}$")) {
            taxIdInput.setError("Invalid format (should be TAXxxx)");
            isValid = false;
        }
        
        return isValid;
    }
}