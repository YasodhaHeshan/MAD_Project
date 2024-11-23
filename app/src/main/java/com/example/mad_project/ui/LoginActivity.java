package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private UserController userController;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        initializeViews();
        
        userController = new UserController(this);
        sessionManager = new SessionManager(this);
        
        // Check if user is already logged in
        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        }

        setupListeners();
    }

    private void initializeViews() {
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.editTextEmail);
        passwordInput = findViewById(R.id.editTextPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnRegister);
    }

    private void setupListeners() {
        loginButton.setOnClickListener(v -> attemptLogin());
        
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        // Reset errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate input
        if (!isValidEmail(email)) {
            emailLayout.setError("Please enter a valid email address");
            return;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Password cannot be empty");
            return;
        }

        // Show loading state
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        userController.login(email, password, user -> {
            runOnUiThread(() -> {
                loginButton.setEnabled(true);
                loginButton.setText("Login");

                if (user != null) {
                    // Save session
                    sessionManager.setLogin(true, email);
                    
                    // Navigate to dashboard
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, 
                        "Invalid email or password", 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}