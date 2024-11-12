package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mad_project.R;
import com.example.mad_project.controller.AppDatabase;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    Button regbutton;
    Button loginButton;
    AppDatabase db;
    UserDao userDao;
    ExecutorService executorService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();

        username = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        regbutton = findViewById(R.id.btnRegister);
        loginButton = findViewById(R.id.btnLogin);

        regbutton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        loginButton.setOnClickListener(v -> {
            String useremail = username.getText().toString();
            String userpassword = password.getText().toString();

            executorService.execute(() -> {
                try {
                    String hashedPassword = hashPassword(useremail, userpassword);
                    User user = userDao.getUserByEmailAndPassword(useremail, hashedPassword);
                    runOnUiThread(() -> {
                        if (user != null) {
                            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            String emailContent = EmailContentGenerator.generateBusTicketEmail(
                                    "User Name", "12345", "Bus123", "City A",
                                    "City B", "2023-10-10", "10:00 AM", "12A"
                            );
                            new EmailService(useremail, "Bus Ticket Confirmation", emailContent,
                                    "senith2002n@gmail.com", "sdkoosopqkskjzce").execute();

                            Toast.makeText(LoginActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (NoSuchAlgorithmException e) {
                    runOnUiThread(() -> Toast.makeText(LoginActivity.this, "Error hashing password", Toast.LENGTH_SHORT).show());
                }
            });
        });
    }

    private String hashPassword(String email, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        String input = email + password;
        byte[] hash = md.digest(input.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }
}