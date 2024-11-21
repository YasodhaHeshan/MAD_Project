package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.mad_project.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class EditProfileActivity extends BaseActivity {
    
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneInput;
    private MaterialButton saveButton;
    private MaterialButton changePhotoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        
        // Setup actionbar
        setupActionBar("Edit Profile", true, false, false);
        
        initializeViews();
        setupListeners();
        loadUserData();
    }

    private void initializeViews() {
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        saveButton = findViewById(R.id.saveButton);
        changePhotoButton = findViewById(R.id.changePhotoButton);
    }

    private void setupListeners() {
        saveButton.setOnClickListener(v -> saveChanges());
        changePhotoButton.setOnClickListener(v -> selectPhoto());
    }

    private void loadUserData() {
        // TODO: Load user data from your database/preferences
        // This is where you'll populate the form with existing user data
    }

    private void saveChanges() {
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();

        // TODO: Validate inputs and save to database
        
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void selectPhoto() {
        // TODO: Implement photo selection logic
        // This could open gallery or camera based on user choice
    }
} 