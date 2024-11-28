package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;
import com.example.mad_project.utils.ImageUtils;
import com.example.mad_project.utils.SessionManager;
import com.example.mad_project.utils.TopUpDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;

public class ProfileActivity extends MainActivity {
    private MaterialButton logoutButton;
    private MaterialButton editProfileButton;
    private MaterialButton registerAsDriverButton;
    private MaterialButton registerAsOwnerButton;
    private MaterialTextView userNameText;
    private MaterialTextView userEmailText;
    private ShapeableImageView profileImage;
    private MaterialTextView profilePointsText;
    private MaterialButton topupButton;
    private SessionManager sessionManager;
    private UserController userController;
    private int currentPoints = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrame);
        setupNavigation(true, true, "Profile");

        sessionManager = new SessionManager(this);
        userController = new UserController(this);

        initializeViews();
        setupClickListeners();
        loadUserPoints();
    }

    private void initializeViews() {
        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        registerAsDriverButton = findViewById(R.id.registerAsDriverButton);
        registerAsOwnerButton = findViewById(R.id.registerAsOwnerButton);
        userNameText = findViewById(R.id.userName);
        userEmailText = findViewById(R.id.userEmail);
        profileImage = findViewById(R.id.profileImage);
        profilePointsText = findViewById(R.id.profilePointsText);
        topupButton = findViewById(R.id.topupButton);

        // Set visibility based on user role
        String role = sessionManager.getRole();
        if ("user".equals(role)) {
            registerAsDriverButton.setVisibility(Button.VISIBLE);
            registerAsOwnerButton.setVisibility(Button.VISIBLE);
        } else if ("driver".equals(role)) {
            registerAsOwnerButton.setVisibility(Button.VISIBLE);
        } else if ("owner".equals(role)) {
            registerAsDriverButton.setVisibility(Button.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        int userId = sessionManager.getUserId();
        if (userId != -1) {
            userController.getUserById(userId, user -> runOnUiThread(() -> {
                if (user != null) {
                    userNameText.setText(user.getName());
                    userEmailText.setText(user.getEmail());
                    ImageUtils.loadProfileImage(this, profileImage, sessionManager.getImage());
                }
            }));
        }
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

        topupButton.setOnClickListener(v -> showTopUpDialog());
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

    private void loadUserPoints() {
        userController.getUserPoints(sessionManager.getUserId(), points -> {
            currentPoints = points;
            runOnUiThread(() -> {
                profilePointsText.setText(String.format("%d Points", points));
            });
        });
    }

    private void showTopUpDialog() {
        TopUpDialog.show(this, currentPoints, points -> {
            userController.addPoints(sessionManager.getUserId(), points, success -> {
                if (success) {
                    loadUserPoints(); // Refresh points display
                    Toast.makeText(this, "Points added successfully", 
                        Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to add points", 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}