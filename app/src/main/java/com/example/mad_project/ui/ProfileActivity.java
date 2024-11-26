package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;
import com.example.mad_project.utils.ImageUtils;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class ProfileActivity extends MainActivity {
    private SessionManager sessionManager;
    private MaterialButton logoutButton;
    private Button editProfileButton;
    private MaterialButton registerAsDriverButton;
    private MaterialButton registerAsOwnerButton;
    private TextView userNameText;
    private TextView userEmailText;
    private ShapeableImageView profileImage;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_profile, contentFrame);
        setupNavigation(true, true, "Profile");

        sessionManager = new SessionManager(this);
        userController = new UserController(this);

        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        logoutButton = findViewById(R.id.logoutButton);
        editProfileButton = findViewById(R.id.editProfileButton);
        registerAsDriverButton = findViewById(R.id.registerAsDriverButton);
        registerAsOwnerButton = findViewById(R.id.registerAsOwnerButton);
        userNameText = findViewById(R.id.userName);
        userEmailText = findViewById(R.id.userEmail);
        profileImage = findViewById(R.id.profileImage);

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