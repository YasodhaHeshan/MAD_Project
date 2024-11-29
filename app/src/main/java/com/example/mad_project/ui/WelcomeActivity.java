package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.utils.RebuildDatabase;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

public class WelcomeActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        sessionManager = new SessionManager(this);
        MaterialButton getStartedButton = findViewById(R.id.getStartedButton);

        getStartedButton.setOnClickListener(v -> {
            startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
            finish();
        });

        RebuildDatabase.clearAndRebuildDatabase(this, true, new RebuildDatabase.RebuildCallback() {
            @Override
            public void onSuccess(String message) {
                sessionManager.setFirstLaunchComplete();
            }

            @Override
            public void onError(String error) {
                // Handle error if database rebuild fails
                runOnUiThread(() -> {
                    Toast.makeText(WelcomeActivity.this, 
                        "Error initializing app: " + error, 
                        Toast.LENGTH_LONG).show();
                });
            }
        });
    }
} 