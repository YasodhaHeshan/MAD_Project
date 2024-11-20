package com.example.mad_project.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.controller.BusDriverController;

import java.util.Calendar;
import java.util.Locale;

public class RegisterBusDriverActivity extends AppCompatActivity {

    private BusDriverController busDriverController;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private long selectedExpiryDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bus_driver);

        busDriverController = new BusDriverController(this);
        setupDatePicker();

        EditText licenseField = findViewById(R.id.editTextLicenseNumber);
        EditText experienceField = findViewById(R.id.editTextExperience);
        Button expiryDateBtn = findViewById(R.id.buttonExpiryDate);
        Button registerButton = findViewById(R.id.buttonRegisterBusDriver);

        expiryDateBtn.setOnClickListener(v -> showDatePicker());

        registerButton.setOnClickListener(v -> {
            String license = licenseField.getText().toString().trim();
            String expStr = experienceField.getText().toString().trim();

            if (license.isEmpty() || expStr.isEmpty() || selectedExpiryDate == 0) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int experience = Integer.parseInt(expStr);
            
            // Get logged in user's ID - replace with your actual method
            int userId = getUserId(); 

            busDriverController.registerBusDriver(
                userId,
                license,
                selectedExpiryDate,
                experience,
                success -> runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Driver registered successfully", 
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

    private void setupDatePicker() {
        dateSetListener = (view, year, month, day) -> {
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            selectedExpiryDate = calendar.getTimeInMillis();
            
            Button expiryDateBtn = findViewById(R.id.buttonExpiryDate);
            expiryDateBtn.setText(String.format(Locale.getDefault(), 
                "%d-%02d-%02d", year, month + 1, day));
        };
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private int getUserId() {
        // Get the user ID from the intent
        Intent intent = getIntent();
        return intent.getIntExtra("userId", -1);
    }
}