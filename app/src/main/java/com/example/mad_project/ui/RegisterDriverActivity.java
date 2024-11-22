package com.example.mad_project.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mad_project.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RegisterDriverActivity extends BaseActivity {

    private TextInputEditText licenseNumberInput;
    private TextInputEditText experienceInput;
    private TextInputEditText expiryDateInput;
    private MaterialButton registerButton;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_driver);

        setupActionBar("Register as Driver", true, false, false);
        initializeViews();
        setupListeners();
    }

    private void initializeViews() {
        licenseNumberInput = findViewById(R.id.licenseNumberInput);
        experienceInput = findViewById(R.id.experienceInput);
        expiryDateInput = findViewById(R.id.expiryDateInput);
        registerButton = findViewById(R.id.registerButton);
        calendar = Calendar.getInstance();
    }

    private void setupListeners() {
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
        String licenseNumber = licenseNumberInput.getText().toString().trim();
        String experience = experienceInput.getText().toString().trim();
        String expiryDate = expiryDateInput.getText().toString().trim();

        // TODO: Implement validation and registration logic
        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
        finish();
    }
} 