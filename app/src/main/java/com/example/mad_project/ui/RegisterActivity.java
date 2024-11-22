package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {

    private TextInputLayout nameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneInput;
    private TextInputEditText passwordInput;
    private MaterialButton registerButton;
    private MaterialButton loginButton;
    private UserController userController;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initializeViews();
        setupListeners();
        
        userController = new UserController(this);
        sessionManager = new SessionManager(this);
    }

    private void initializeViews() {
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);
    }

    private void setupListeners() {
        registerButton.setOnClickListener(v -> attemptRegistration());
        loginButton.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptRegistration() {
        // Reset errors
        nameLayout.setError(null);
        emailLayout.setError(null);
        phoneLayout.setError(null);
        passwordLayout.setError(null);

        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String phone = phoneInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (name.isEmpty()) {
            nameLayout.setError("Name cannot be empty");
            return;
        }

        if (!isValidEmail(email)) {
            emailLayout.setError("Please enter a valid email address");
            return;
        }

        if (phone.isEmpty()) {
            phoneLayout.setError("Phone number cannot be empty");
            return;
        }

        if (password.isEmpty()) {
            passwordLayout.setError("Password cannot be empty");
            return;
        }

        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Registering...");

        userController.register(name, email, phone, password, "user", success -> {
            runOnUiThread(() -> {
                registerButton.setEnabled(true);
                registerButton.setText("Register");

                if (success) {
                    // Save session
                    sessionManager.setLogin(true, email);
                    
                    // Navigate to dashboard
                    Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, 
                        "Registration failed. Email might already be registered.", 
                        Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private boolean isValidEmail(String email) {
        return !email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
