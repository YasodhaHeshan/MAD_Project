package com.example.mad_project.controller;

import static com.example.mad_project.controller.HashPassword.hashPassword;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.ui.DashboardActivity;
import com.example.mad_project.ui.LoginActivity;
import com.example.mad_project.utils.EmailSender;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class UserController {

    private final AppDatabase db;
    private final UserDao userDao;

    public UserController(Context context) {
        db = Room.databaseBuilder(context, AppDatabase.class, "mad_project_db").build();
        userDao = db.userDao();
    }

    public void register(String firstName, String lastName, String email, String mobile, String password, String confirmPassword, Context context) {
        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty() || mobile.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(context, "Password must be at least 8 characters long", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPassword)) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show();
        } else {
            new Thread(() -> {
                try {
                    String passwordHash = hashPassword(email, password);
                    User newUser = new User(firstName, lastName, email, mobile, passwordHash);
                    userDao.insert(newUser);

                    // Send email
                    String subject = "Registration Successful";
                    String message = "Dear " + firstName + ",\n\nYour registration was successful!";
                    String accessToken = "your_access_token";
                    EmailSender.sendEmail(context, email, subject, message, accessToken);

                    // Redirect to Login
                    ((Activity) context).runOnUiThread(() -> {
                        Toast.makeText(context, "User registered successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    });
                } catch (Exception e) {
                    Log.e("UserController", "Failed to register user", e);
                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Failed to register user: " + e.getMessage(), Toast.LENGTH_LONG).show());
                } finally {
                    db.close();
                }
            }).start();
        }
    }

    public void login(String email, String password, Context context) {
        new Thread(() -> {
            try {
                if (email.isEmpty() || password.isEmpty()) {
                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show());
                } else if (userDao.getUserByEmail(email) == null) {
                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show());
                } else if (!userDao.getUserByEmail(email).getPassword().equals(hashPassword(email, password))) {
                    ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Incorrect username or password", Toast.LENGTH_SHORT).show());
                } else {
                    ((Activity) context).runOnUiThread(() -> {
                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, DashboardActivity.class);
                        context.startActivity(intent);
                    });
                }
            } catch (Exception e) {
                Log.e("UserController", "Failed to login user", e);
                ((Activity) context).runOnUiThread(() -> Toast.makeText(context, "Failed to login user: " + e.getMessage(), Toast.LENGTH_LONG).show());
            } finally {
                db.close();
            }
        }).start();
    }
}