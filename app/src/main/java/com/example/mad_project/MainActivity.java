package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.ui.BusActivity;
import com.example.mad_project.ui.DashboardActivity;
import com.example.mad_project.ui.DebugMenuActivity;
import com.example.mad_project.utils.FillDatabase;
import com.example.mad_project.ui.LoginActivity;
import com.example.mad_project.ui.ProfileActivity;
import com.example.mad_project.ui.RegisterActivity;
import com.example.mad_project.ui.TicketsActivity;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    protected SessionManager sessionManager;
    protected MaterialToolbar topAppBar;
    protected BottomNavigationView bottomNavigationView;
    protected AppBarLayout appBarLayout;
    protected FrameLayout contentFrame;
    
    // Set to true to show debug features
    protected static final boolean DEBUG_MODE = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        Log.d("MainActivity", "onCreate started");

        // Initialize SessionManager
        sessionManager = new SessionManager(this);
        
        // Register back press callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // If this is a main screen, finish the activity
                if (MainActivity.this instanceof DashboardActivity ||
                    MainActivity.this instanceof TicketsActivity ||
                    MainActivity.this instanceof BusActivity ||
                    MainActivity.this instanceof ProfileActivity) {
                    finish();
                } else {
                    // Remove this callback and let the system handle back
                    this.remove();
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });

        // Only handle welcome screen logic if we're actually in MainActivity
        if (getClass() == MainActivity.class) {
            if (!sessionManager.isLoggedIn()) {
                Log.d("MainActivity", "Setting welcome screen layout");
                setContentView(R.layout.activity_main);
                setupWelcomeScreen();
                return;
            }
            setContentView(R.layout.activity_base);
            setupBaseViews();
            redirectToDashboard();
        } else {
            // For child activities, just set up the base layout
            setContentView(R.layout.activity_base);
            setupBaseViews();
        }
    }

    protected void setupBaseViews() {
        Log.d("MainActivity", "Setting up base views");
        try {
            // Initialize views
            appBarLayout = findViewById(R.id.appBarLayout);
            topAppBar = findViewById(R.id.topAppBar);
            bottomNavigationView = findViewById(R.id.bottomNavigationView);
            contentFrame = findViewById(R.id.content_frame);

            // Hide navigation by default for child activities
            if (appBarLayout != null) appBarLayout.setVisibility(View.GONE);
            if (bottomNavigationView != null) bottomNavigationView.setVisibility(View.GONE);

        } catch (Exception e) {
            Log.e("MainActivity", "Error in setupBaseViews: " + e.getMessage());
        }
    }

    protected void redirectToDashboard() {
        // If logged in, redirect to Dashboard
        Log.d("MainActivity", "User logged in, redirecting to DashboardActivity");
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    protected void redirectToLogin() {
        // If not logged in, redirect to Login
        Log.d("MainActivity", "User not logged in, redirecting to LoginActivity");
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setupWelcomeScreen() {
        Log.d("MainActivity", "Setting up welcome screen");
        try {
            View loginButton = findViewById(R.id.btnLogin);
            View registerButton = findViewById(R.id.btnRegister);
            View fillDatabaseButton = findViewById(R.id.btnFillDatabase);

            if (loginButton != null) {
                Log.d("MainActivity", "Login button found");
                loginButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                });
            } else {
                Log.e("MainActivity", "Login button not found!");
            }

            if (registerButton != null) {
                Log.d("MainActivity", "Register button found");
                registerButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                });
            } else {
                Log.e("MainActivity", "Register button not found!");
            }

            // Show Fill Database button in debug mode
            if (fillDatabaseButton != null && DEBUG_MODE) {
                Log.d("MainActivity", "Fill database button found and debug mode enabled");
                fillDatabaseButton.setVisibility(View.VISIBLE);
                fillDatabaseButton.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, FillDatabase.class);
                    startActivity(intent);
                });
            } else {
                Log.d("MainActivity", "Fill database button not found or debug mode disabled");
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in setupWelcomeScreen: " + e.getMessage());
        }
    }

    protected void setupNavigation(boolean showToolbar, boolean showBottomNav, String title) {
        try {
            if (showToolbar && topAppBar != null && appBarLayout != null) {
                appBarLayout.setVisibility(View.VISIBLE);
                topAppBar.setTitle(title);
                
                // Show back button if not on main screens
                if (!(this instanceof DashboardActivity) && 
                    !(this instanceof TicketsActivity) && 
                    !(this instanceof BusActivity) && 
                    !(this instanceof ProfileActivity)) {
                    topAppBar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
                    topAppBar.setNavigationOnClickListener(v -> 
                        getOnBackPressedDispatcher().onBackPressed()
                    );
                } else {
                    topAppBar.setNavigationIcon(null);
                }
            }
            
            if (bottomNavigationView != null) {
                bottomNavigationView.setVisibility(showBottomNav ? View.VISIBLE : View.GONE);
                if (showBottomNav) {
                    setupBottomNavigationListener();
                }
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error in setupNavigation: " + e.getMessage());
        }
    }

    private void setupBottomNavigationListener() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Intent intent = null;
            
            // Don't create new activity if we're already on it
            if (this.getClass().equals(getDestinationActivity(item.getItemId()))) {
                return true;
            }

            if (item.getItemId() == R.id.navigation_home) {
                intent = new Intent(this, DashboardActivity.class);
            } else if (item.getItemId() == R.id.navigation_tickets) {
                intent = new Intent(this, TicketsActivity.class);
            } else if (item.getItemId() == R.id.navigation_buses) {
                intent = new Intent(this, BusActivity.class);
            } else if (item.getItemId() == R.id.navigation_profile) {
                intent = new Intent(this, ProfileActivity.class);
            }

            if (intent != null) {
                // Clear back stack when switching main tabs
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private Class<?> getDestinationActivity(int itemId) {
        if (itemId == R.id.navigation_home) {
            return DashboardActivity.class;
        } else if (itemId == R.id.navigation_tickets) {
            return TicketsActivity.class;
        } else if (itemId == R.id.navigation_buses) {
            return BusActivity.class;
        } else if (itemId == R.id.navigation_profile) {
            return ProfileActivity.class;
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update bottom navigation selection based on current activity
        if (bottomNavigationView != null) {
            int selectedItem;
            if (this instanceof DashboardActivity) {
                selectedItem = R.id.navigation_home;
            } else if (this instanceof TicketsActivity) {
                selectedItem = R.id.navigation_tickets;
            } else if (this instanceof BusActivity) {
                selectedItem = R.id.navigation_buses;
            } else if (this instanceof ProfileActivity) {
                selectedItem = R.id.navigation_profile;
            } else {
                selectedItem = -1;
            }
            if (selectedItem != -1) {
                bottomNavigationView.setSelectedItemId(selectedItem);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (appBarLayout.getVisibility() == View.VISIBLE) {
            getMenuInflater().inflate(R.menu.top_app_bar, menu);
            
            // Show debug menu in debug mode
            MenuItem debugItem = menu.findItem(R.id.action_debug);
            if (debugItem != null) {
                debugItem.setVisible(DEBUG_MODE);
            }
            
            return true;
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            sessionManager.logout();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        } else if (item.getItemId() == R.id.action_debug) {
            startActivity(new Intent(this, DebugMenuActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}