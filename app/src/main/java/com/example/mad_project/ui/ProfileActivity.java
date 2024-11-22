package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.google.android.material.button.MaterialButton;

public class ProfileActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button editProfileButton = findViewById(R.id.editProfileButton);
        MaterialButton registerAsDriverButton = findViewById(R.id.registerAsDriverButton);
        MaterialButton registerAsOwnerButton = findViewById(R.id.registerAsOwnerButton);
        
        // Setup actionbar with title, show back button only
        setupActionBar("Profile", true, false, false);

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
}