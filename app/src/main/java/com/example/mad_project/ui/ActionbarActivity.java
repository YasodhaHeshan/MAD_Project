package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.mad_project.R;
import com.google.android.material.button.MaterialButton;

public class ActionbarActivity extends AppCompatActivity {

    protected Toolbar toolbar;
    protected TextView toolbarTitle;
    protected MaterialButton backButton;
    protected MaterialButton notificationButton;
    protected MaterialButton settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initializeToolbar();
    }

    private void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            
            toolbarTitle = toolbar.findViewById(R.id.toolbarTitle);
            backButton = toolbar.findViewById(R.id.backButton);
            notificationButton = toolbar.findViewById(R.id.notificationButton);
            settingsButton = toolbar.findViewById(R.id.settingsButton);

            setupDefaultClickListeners();
        }
    }

    private void setupDefaultClickListeners() {
        if (backButton != null) {
            backButton.setOnClickListener(v -> onBackPressed());
        }

        if (settingsButton != null) {
            settingsButton.setOnClickListener(v -> {
                Intent intent = new Intent(this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        if (notificationButton != null) {
            notificationButton.setOnClickListener(v -> {
                // TODO: Implement notifications
            });
        }
    }

    protected void setupActionBar(String title, boolean showBackButton, 
            boolean showNotification, boolean showSettings) {
        if (toolbar == null) return;

        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }

        if (backButton != null) {
            backButton.setVisibility(showBackButton ? View.VISIBLE : View.GONE);
        }

        if (notificationButton != null) {
            notificationButton.setVisibility(showNotification ? View.VISIBLE : View.GONE);
        }

        if (settingsButton != null) {
            settingsButton.setVisibility(showSettings ? View.VISIBLE : View.GONE);
        }
    }
}
