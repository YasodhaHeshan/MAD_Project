package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends BaseActivity {
    private SessionManager sessionManager;
    private MaterialButton logoutButton;
    private Button editProfileButton;
    private MaterialButton registerAsDriverButton;
    private MaterialButton registerAsOwnerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sessionManager = new SessionManager(this);

        // Initialize views
        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        registerAsDriverButton = findViewById(R.id.registerAsDriverButton);
        registerAsOwnerButton = findViewById(R.id.registerAsOwnerButton);

        // Setup actionbar with title, show back button only
        setupActionBar("Profile", true, false, false);

        setupClickListeners();
    }

    private void setupClickListeners() {
        logoutButton.setOnClickListener(v -> handleLogout());

        // Existing click listeners...
        editProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            startActivity(intent);
        });

        registerAsDriverButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, RegisterDriverActivity.class);
            startActivity(intent);
        });

        registerAsOwnerButton.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, RegisterOwnerActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogout() {
        // Clear the session
        sessionManager.logout();

        // Navigate to login screen
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        // Clear the activity stack so user can't go back
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}