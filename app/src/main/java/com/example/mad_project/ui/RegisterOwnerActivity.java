package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;
import com.example.mad_project.utils.Validation;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterOwnerActivity extends AppCompatActivity {
    private TextInputLayout companyNameLayout;
    private TextInputLayout registrationNumberLayout;
    private TextInputLayout taxIdLayout;

    private TextInputEditText companyNameInput;
    private TextInputEditText registrationNumberInput;
    private TextInputEditText taxIdInput;
    private MaterialButton registerButton;

    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        initializeViews();
        setupListeners();

        userController = new UserController(this);
    }

    private void initializeViews() {
        companyNameLayout = findViewById(R.id.companyNameLayout);
        registrationNumberLayout = findViewById(R.id.registrationNumberLayout);
        taxIdLayout = findViewById(R.id.taxIdLayout);

        companyNameInput = findViewById(R.id.companyNameInput);
        registrationNumberInput = findViewById(R.id.registrationNumberInput);
        taxIdInput = findViewById(R.id.taxIdInput);
        registerButton = findViewById(R.id.registerButton);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> attemptRegistration());
    }

    private void attemptRegistration() {
        String companyName = companyNameInput.getText().toString().trim();
        String registrationNumber = registrationNumberInput.getText().toString().trim();
        String taxId = taxIdInput.getText().toString().trim();

        // Reset errors
        companyNameLayout.setError(null);
        registrationNumberLayout.setError(null);
        taxIdLayout.setError(null);

        // Validate inputs
        if (companyName.isEmpty()) {
            companyNameLayout.setError("Company name is required");
            return;
        }

        if (!Validation.isValidCompanyRegistration(registrationNumber)) {
            registrationNumberLayout.setError("Please enter a valid registration number (e.g., AB12345)");
            return;
        }

        if (!Validation.isValidTaxId(taxId)) {
            taxIdLayout.setError("Please enter a valid tax ID (e.g., ABC1234567)");
            return;
        }

        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        userController.upgradeToOwner(
            companyName, registrationNumber, taxId,
            success -> runOnUiThread(() -> {
                registerButton.setEnabled(true);
                registerButton.setText("Register");

                if (success) {
                    Toast.makeText(this, "Successfully registered as a bus owner", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, LoginActivity.class));
                    finishAffinity();
                } else {
                    Toast.makeText(this, "Registration failed. Company registration number may already be registered.",
                        Toast.LENGTH_LONG).show();
                }
            })
        );
    }
}