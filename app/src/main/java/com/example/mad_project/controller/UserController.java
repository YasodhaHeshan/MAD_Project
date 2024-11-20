package com.example.mad_project.controller;

import static com.example.mad_project.utils.HashPassword.hashPassword;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.room.Room;

import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.User;
import com.example.mad_project.data.UserDao;
import com.example.mad_project.utils.EmailSender;
import com.example.mad_project.utils.HashPassword;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class UserController {

    private final AppDatabase db;
    private final UserDao userDao;
    private final ExecutorService executorService;

    public UserController(Context context) {
        db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "mad_project_db").build();
        userDao = db.userDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public void register(String name, String email, String phone, String password, String role, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                if (userDao.isEmailExists(email)) {
                    callback.accept(false);
                    return;
                }

                String hashedPassword = hashPassword(password);
                User user = new User(name, email, phone, hashedPassword, role);
                userDao.insert(user);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("UserController", "Error registering user", e);
                callback.accept(false);
            }
        });
    }

    public void login(String email, String password, Consumer<User> callback) {
        executorService.execute(() -> {
            try {
                User user = userDao.getUserByEmail(email);
                if (user != null && HashPassword.verifyPassword(password, user.getPassword())) {
                    callback.accept(user);
                } else {
                    callback.accept(null);
                }
            } catch (Exception e) {
                Log.e("UserController", "Error logging in", e);
                callback.accept(null);
            }
        });
    }

    public void updateUser(User user, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                user.setUpdatedAt(System.currentTimeMillis());
                userDao.update(user);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("UserController", "Error updating user", e);
                callback.accept(false);
            }
        });
    }

    public void deactivateUser(int userId, Consumer<Boolean> callback) {
        executorService.execute(() -> {
            try {
                userDao.deactivateUser(userId);
                callback.accept(true);
            } catch (Exception e) {
                Log.e("UserController", "Error deactivating user", e);
                callback.accept(false);
            }
        });
    }
}