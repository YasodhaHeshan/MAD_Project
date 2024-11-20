package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;

import com.example.mad_project.controller.BusOwnerController;
import com.example.mad_project.data.User;

public class RegisterOwnerActivity extends AppCompatActivity {

    private BusOwnerController busOwnerController;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bus_owner);

        busOwnerController = new BusOwnerController(this);
        currentUser = (User) getIntent().getSerializableExtra("user");

        EditText companyNameField = findViewById(R.id.editTextCompanyName);
        EditText registrationField = findViewById(R.id.editTextRegistration);
        EditText taxIdField = findViewById(R.id.editTextTaxId);
        Button registerButton = findViewById(R.id.buttonRegisterOwner);

        registerButton.setOnClickListener(v -> {
            String companyName = companyNameField.getText().toString().trim();
            String registration = registrationField.getText().toString().trim();
            String taxId = taxIdField.getText().toString().trim();

            if (companyName.isEmpty() || registration.isEmpty() || taxId.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            busOwnerController.registerBusOwner(
                currentUser.getId(),
                companyName,
                registration,
                taxId,
                success -> runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Bus owner registered successfully", 
                            Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed. Please try again.", 
                            Toast.LENGTH_SHORT).show();
                    }
                })
            );
        });
    }
}