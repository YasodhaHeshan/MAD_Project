package com.example.mad_project.ui;

import android.os.Bundle;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterOwnerActivity extends MainActivity {

    private TextInputEditText companyNameInput;
    private TextInputEditText registrationNumberInput;
    private TextInputEditText taxIdInput;
    private MaterialButton registerButton;

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
        String companyName = companyNameInput.getText().toString().trim();
        String registrationNumber = registrationNumberInput.getText().toString().trim();
        String taxId = taxIdInput.getText().toString().trim();

        // TODO: Implement validation and registration logic
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        finish();
    }
}