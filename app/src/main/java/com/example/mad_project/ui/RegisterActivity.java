package com.example.mad_project.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mad_project.MainActivity;
import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;
import com.example.mad_project.utils.SessionManager;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class RegisterActivity extends MainActivity {

    private TextInputLayout nameLayout;
    private TextInputLayout emailLayout;
    private TextInputLayout phoneLayout;
    private TextInputLayout passwordLayout;
    private TextInputLayout confirmPasswordLayout;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private MaterialButton registerButton;
    private TextView loginButton;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Inflate the register layout into the content frame
        getLayoutInflater().inflate(R.layout.activity_register, contentFrame);
        
        // Initialize views and setup listeners
        initializeViews();
        setupInputValidation();
        setupClickListeners();
        
        // Set up the toolbar with back button after views are initialized
        setupNavigation(true, false, "Register");
    }

    private void initializeViews() {
        // Initialize TextInputLayouts
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneLayout = findViewById(R.id.phoneLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        // Initialize EditTexts
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        phoneInput = findViewById(R.id.phoneInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        // Initialize buttons
        registerButton = findViewById(R.id.registerButton);
        loginButton = findViewById(R.id.loginButton);

        // Initialize controllers
        userController = new UserController(this);
        sessionManager = new SessionManager(this);
    }

    private void setupClickListeners() {
        registerButton.setOnClickListener(v -> handleRegistration());
        loginButton.setOnClickListener(v -> {
            // Navigate to login activity
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void setupInputValidation() {
        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validateEmail(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        phoneInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePhone(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        confirmPasswordInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePasswords();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void validateName(String name) {
        if (name.trim().isEmpty()) {
            nameLayout.setError("Name is required");
            nameLayout.setBoxStrokeColor(Color.RED);
        } else {
            nameLayout.setError(null);
            nameLayout.setBoxStrokeColor(Color.WHITE);
        }
    }

    private void validateEmail(String email) {
        if (email.trim().isEmpty()) {
            emailLayout.setError("Email is required");
            emailLayout.setBoxStrokeColor(Color.RED);
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Invalid email format");
            emailLayout.setBoxStrokeColor(Color.RED);
        } else {
            emailLayout.setError(null);
            emailLayout.setBoxStrokeColor(Color.WHITE);
        }
    }

    private void validatePhone(String phone) {
        if (phone.trim().isEmpty()) {
            phoneLayout.setError("Phone number is required");
            phoneLayout.setBoxStrokeColor(Color.RED);
        } else if (phone.length() != 10) {
            phoneLayout.setError("Phone number must be 10 digits");
            phoneLayout.setBoxStrokeColor(Color.RED);
        } else {
            phoneLayout.setError(null);
            phoneLayout.setBoxStrokeColor(Color.WHITE);
        }
    }

    private void validatePasswords() {
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        String confirmPassword = Objects.requireNonNull(confirmPasswordInput.getText()).toString();

        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            passwordLayout.setBoxStrokeColor(Color.RED);
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            passwordLayout.setBoxStrokeColor(Color.RED);
        } else {
            passwordLayout.setError(null);
            passwordLayout.setBoxStrokeColor(Color.WHITE);
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            confirmPasswordLayout.setBoxStrokeColor(Color.RED);
        } else {
            confirmPasswordLayout.setError(null);
            confirmPasswordLayout.setBoxStrokeColor(Color.WHITE);
        }
    }

    private boolean validateInputs() {
        String name = Objects.requireNonNull(nameInput.getText()).toString().trim();
        String email = Objects.requireNonNull(emailInput.getText()).toString().trim();
        String phone = Objects.requireNonNull(phoneInput.getText()).toString().trim();
        String password = Objects.requireNonNull(passwordInput.getText()).toString();
        String confirmPassword = Objects.requireNonNull(confirmPasswordInput.getText()).toString();

        // Validate all fields
        validateName(name);
        validateEmail(email);
        validatePhone(phone);
        validatePasswords();

        // Check if there are any errors
        if (nameLayout.getError() != null || emailLayout.getError() != null || 
            phoneLayout.getError() != null || passwordLayout.getError() != null || 
            confirmPasswordLayout.getError() != null) {
            Toast.makeText(this, "Please fix the errors before registering", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void handleRegistration() {
        if (validateInputs()) {
            // Get user input
            String name = Objects.requireNonNull(nameInput.getText()).toString().trim();
            String email = Objects.requireNonNull(emailInput.getText()).toString().trim();
            String phone = Objects.requireNonNull(phoneInput.getText()).toString().trim();
            String password = Objects.requireNonNull(passwordInput.getText()).toString().trim();

            // Show processing message
            Toast.makeText(this, "Processing registration...", Toast.LENGTH_SHORT).show();

            // Attempt registration
            userController.register(name, email, phone, password, "user", success -> runOnUiThread(() -> {
                if (success) {
                    Toast.makeText(RegisterActivity.this, "Registration successful! Please login.", Toast.LENGTH_SHORT).show();
                    redirectToLogin();
                } else {
                    Toast.makeText(RegisterActivity.this, "Registration failed. Email might already be registered.", Toast.LENGTH_SHORT).show();
                }
            }));
        }
    }

    @Override
    protected void redirectToLogin() {
        super.redirectToLogin();
    }
}
