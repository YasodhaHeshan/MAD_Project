package com.example.mad_project.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import android.os.Handler;
import android.os.Looper;

import android.view.View;
import android.widget.TextView;

import com.example.mad_project.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public abstract class BaseActivity extends AppCompatActivity {
    protected BottomNavigationView bottomNavigationView;
    protected Toolbar toolbar;
    protected TextView toolbarTitle;
    protected MaterialButton backButton;
    protected MaterialButton notificationButton;
    protected MaterialButton settingsButton;
    
    private boolean isNavigating = false;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isNavigating) return;
                isNavigating = true;
                
                // First update the UI state
                updateNavigationState();
                
                // Then perform the actual navigation after a frame
                new Handler(Looper.getMainLooper()).post(() -> {
                    isNavigating = false;
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                });
            }
        });
    }
    
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        initializeToolbar();
        initializeNavbar();
    }
    
    protected void initializeToolbar() {
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
    
    protected void setupDefaultClickListeners() {
        if (backButton != null) {
            backButton.setOnClickListener(v -> 
                getOnBackPressedDispatcher().onBackPressed());
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

    protected void initializeNavbar() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        if (bottomNavigationView != null) {
            bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.black));
            bottomNavigationView.setItemTextColor(getResources().getColorStateList(R.color.nav_item_color));
            bottomNavigationView.setItemIconTintList(getResources().getColorStateList(R.color.nav_item_color));
            
            setupNavigation();
        }
    }
    
    protected void setupNavigation() {
        if (bottomNavigationView == null) return;
        
        // Set initial state
        int currentItemId = getNavigationItemForActivity(getClass().getName());
        if (currentItemId != -1) {
            bottomNavigationView.setSelectedItemId(currentItemId);
        }
        
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (isNavigating) return false;
            
            Intent intent = getIntentForNavigationItem(item.getItemId());
            if (intent != null && !this.getClass().equals(intent.getComponent().getClass())) {
                isNavigating = true;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                isNavigating = false;
                return true;
            }
            return false;
        });
    }

    private Intent getIntentForNavigationItem(int itemId) {
        if (itemId == R.id.navigation_home) {
            return new Intent(this, DashboardActivity.class);
        } else if (itemId == R.id.navigation_social) {
            return new Intent(this, TicketsActivity.class);
        } else if (itemId == R.id.navigation_payment) {
            return new Intent(this, PaymentActivity.class);
        } else if (itemId == R.id.navigation_profile) {
            return new Intent(this, ProfileActivity.class);
        }
        return null;
    }

    private void updateNavigationState() {
        Intent upIntent = NavUtils.getParentActivityIntent(this);
        if (upIntent != null && bottomNavigationView != null) {
            ComponentName parentActivity = upIntent.getComponent();
            if (parentActivity != null) {
                String className = parentActivity.getClassName();
                int navItemId = getNavigationItemForActivity(className);
                if (navItemId != -1) {
                    bottomNavigationView.setSelectedItemId(navItemId);
                }
            }
        }
    }

    private int getNavigationItemForActivity(String className) {
        if (className.contains("DashboardActivity")) {
            return R.id.navigation_home;
        } else if (className.contains("TicketsActivity")) {
            return R.id.navigation_social;
        } else if (className.contains("PaymentActivity")) {
            return R.id.navigation_payment;
        } else if (className.contains("ProfileActivity")) {
            return R.id.navigation_profile;
        }
        return -1;
    }
}
