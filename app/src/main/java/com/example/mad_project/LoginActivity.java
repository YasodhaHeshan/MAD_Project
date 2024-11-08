package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button regbutton ;
    Button loginButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        regbutton = findViewById(R.id.btnRegister);
        loginButton = findViewById(R.id.btnLogin);



        regbutton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
            String useremail = username.getText().toString();
            String emailContent = EmailContentGenerator.generateBusTicketEmail(
                    "User Name", "12345", "Bus123", "City A",
                    "City B", "2023-10-10", "10:00 AM", "12A"
            );
            new EmailService(useremail, "Bus Ticket Confirmation", emailContent,
                    "senith2002n@gmail.com", "sdkoosopqkskjzce").execute();

            Toast.makeText(this, "Email Sent", Toast.LENGTH_SHORT).show();

        });
    }
}
