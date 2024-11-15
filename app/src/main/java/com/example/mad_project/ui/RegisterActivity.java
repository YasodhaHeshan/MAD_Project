package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

        // Set onClickListeners for the buttons
        customerButton.setOnClickListener(v -> {
            updateHints("Customer First Name", "Customer Last Name", "Customer Email", "Customer Mobile", "Customer Password", "Confirm Customer Password");
            updateButtonColors(customerButton, ownerButton, driverButton);
        });

        ownerButton.setOnClickListener(v -> runOnUiThread(() -> {
            Intent intent = new Intent(RegisterActivity.this, RegisterOwnerActivity.class);
            startActivity(intent);
        }));

        driverButton.setOnClickListener(v -> {
            updateHints("Driver First Name", "Driver Last Name", "Driver Email", "Driver Mobile", "Driver Password", "Confirm Driver Password");
            updateButtonColors(driverButton, customerButton, ownerButton);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signupButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String email = emailEditText.getText().toString();
            String mobile = mobileEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String confirmPassword = confirmPasswordEditText.getText().toString();

            // Register the user
            UserController userController = new UserController(this);
            userController.register(firstName, lastName, email, mobile, password, confirmPassword, this);
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
