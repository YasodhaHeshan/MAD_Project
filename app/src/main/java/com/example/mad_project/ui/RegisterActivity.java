package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mad_project.R;
import com.example.mad_project.controller.UserController;

public class RegisterActivity extends AppCompatActivity {

    private EditText firstNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private EditText mobileEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button customerButton;
    private Button ownerButton;
    private Button driverButton;
    private UserController userController;
    private String selectedRole = "customer"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize the EditText fields
        firstNameEditText = findViewById(R.id.editTextText2);
        lastNameEditText = findViewById(R.id.editTextText4);
        emailEditText = findViewById(R.id.editTextText5);
        mobileEditText = findViewById(R.id.editTextText6);
        passwordEditText = findViewById(R.id.editTextText7);
        confirmPasswordEditText = findViewById(R.id.editTextText8);

        // Initialize the buttons
        customerButton = findViewById(R.id.btnCustomer);
        ownerButton = findViewById(R.id.btnOwner);
        driverButton = findViewById(R.id.btnDriver);
        Button loginButton = findViewById(R.id.btnLogin);
        Button signupButton = findViewById(R.id.btnSignup);

        userController = new UserController(this);

        // Set onClickListeners for the buttons
        customerButton.setOnClickListener(v -> {
            selectedRole = "customer";
            updateHints("Customer First Name", "Customer Last Name", "Customer Email", "Customer Mobile", "Customer Password", "Confirm Customer Password");
            updateButtonColors(customerButton, ownerButton, driverButton);
        });

        ownerButton.setOnClickListener(v -> runOnUiThread(() -> {
            Intent intent = new Intent(RegisterActivity.this, RegisterOwnerActivity.class);
            startActivity(intent);
        }));

        driverButton.setOnClickListener(v -> {
            selectedRole = "driver";
            updateHints("Driver First Name", "Driver Last Name", "Driver Email", "Driver Mobile", "Driver Password", "Confirm Driver Password");
            updateButtonColors(driverButton, customerButton, ownerButton);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        signupButton.setOnClickListener(v -> {
            String name = firstNameEditText.getText().toString() + " " + 
                         lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String phone = mobileEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();
            
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }
            
            userController.register(name, email, phone, password, selectedRole, success -> {
                runOnUiThread(() -> {
                    if (success) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });
    }

    private void updateHints(String firstNameHint, String lastNameHint, String emailHint, String mobileHint, String passwordHint, String confirmPasswordHint) {
        firstNameEditText.setHint(firstNameHint);
        lastNameEditText.setHint(lastNameHint);
        emailEditText.setHint(emailHint);
        mobileEditText.setHint(mobileHint);
        passwordEditText.setHint(passwordHint);
        confirmPasswordEditText.setHint(confirmPasswordHint);
    }

    private void updateButtonColors(Button selectedButton, Button button1, Button button2) {
        selectedButton.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.white));
        selectedButton.setTextColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.black));
        button1.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.black));
        button1.setTextColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.white));
        button2.setBackgroundColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.black));
        button2.setTextColor(ContextCompat.getColor(RegisterActivity.this, android.R.color.white));
    }
}
