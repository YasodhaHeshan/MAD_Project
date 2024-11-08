package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.data.DatabaseHelper;

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
        setContentView(R.layout.registercus);

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
        Button login = findViewById(R.id.btnLogin);
        Button signup = findViewById(R.id.btnSignup);

        // Set onClickListeners for the buttons
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHints("Customer First Name", "Customer Last Name", "Customer Email", "Customer Mobile", "Customer Password", "Confirm Customer Password");
                updateButtonColors(customerButton, ownerButton, driverButton);
            }
        });

        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHints("Owner First Name", "Owner Last Name", "Owner Email", "Owner Mobile", "Owner Password", "Confirm Owner Password");
                updateButtonColors(ownerButton, customerButton, driverButton);
            }
        });

        driverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateHints("Driver First Name", "Driver Last Name", "Driver Email", "Driver Mobile", "Driver Password", "Confirm Driver Password");
                updateButtonColors(driverButton, customerButton, ownerButton);
            }
        });

        login.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        signup.setOnClickListener(v -> {
            String firstNameStr = firstNameEditText.getText().toString();
            String lastNameStr = lastNameEditText.getText().toString();
            String emailStr = emailEditText.getText().toString();
            String passwordStr = passwordEditText.getText().toString();
            String mobileStr = mobileEditText.getText().toString();

            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.addUser(firstNameStr, lastNameStr, emailStr, passwordStr, mobileStr);

            Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, PaymentActivity.class);
            startActivity(intent);
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
        selectedButton.setBackgroundColor(getResources().getColor(android.R.color.white));
        selectedButton.setTextColor(getResources().getColor(android.R.color.black));
        button1.setBackgroundColor(getResources().getColor(android.R.color.black));
        button1.setTextColor(getResources().getColor(android.R.color.white));
        button2.setBackgroundColor(getResources().getColor(android.R.color.black));
        button2.setTextColor(getResources().getColor(android.R.color.white));
    }
}