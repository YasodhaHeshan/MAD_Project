package com.example.busbook.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.busbook.R;
import com.example.busbook.controller.BusDriverController;

public class RegisterBusDriverActivity extends AppCompatActivity {

    private final BusDriverController busDriverController = new BusDriverController(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bus_driver);

        EditText licenseNumber = findViewById(R.id.editTextLicenseNumber);
        EditText nic = findViewById(R.id.editTextNIC);
        Button registerBusDriverButton = findViewById(R.id.buttonRegisterBusDriver);

        registerBusDriverButton.setOnClickListener(v -> {
            String licenseNumberText = licenseNumber.getText().toString();
            String nicText = nic.getText().toString();

            busDriverController.registerBusDriver(licenseNumberText, nicText, this);
        });
    }
}