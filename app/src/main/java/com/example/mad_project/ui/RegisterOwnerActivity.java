package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;

import com.example.mad_project.controller.BusOwnerController;

public class RegisterOwnerActivity extends AppCompatActivity {

    private final BusOwnerController busOwnerController = new BusOwnerController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bus_owner);
        EditText companyName = findViewById(R.id.editTextCompanyName);
        EditText licenseNumber = findViewById(R.id.editTextLicenseNumber);
        EditText nic = findViewById(R.id.editTextNIC);
        Button registerOwnerButton = findViewById(R.id.buttonRegisterOwner);
        Button buttonAddBus = findViewById(R.id.buttonAddBus);

        // Initialize ButtonRegisterOwner
        registerOwnerButton.setOnClickListener(v -> {
            String companyNameText = companyName.getText().toString();
            String licenseNumberText = licenseNumber.getText().toString();
            String nicText = nic.getText().toString();

            busOwnerController.registerBusOwner(companyNameText, licenseNumberText, nicText, this);
        });

        // Initialize ButtonAddBus
        buttonAddBus.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterOwnerActivity.this, RegisterBusActivity.class);
            startActivity(intent);
        });
    }
}