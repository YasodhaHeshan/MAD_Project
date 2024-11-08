// File: app/src/main/java/com/example/mad_project/data/DatabaseHelper.java
package com.example.mad_project.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MAD_Project.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create tables
        String CREATE_CUSTOMER_TABLE = "CREATE TABLE Customer (id INTEGER PRIMARY KEY, firstname TEXT, lastname TEXT, email TEXT, password TEXT, mobile Number)";
        db.execSQL(CREATE_CUSTOMER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS bus");
        // Create tables again
        onCreate(db);
    }

    public void addUser(String firstName, String lastName, String email, String password, String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstname", firstName);
        values.put("lastname", lastName);
        values.put("email", email);
        values.put("password", password);
        values.put("mobile", mobile);
        db.insert("Customer", null, values);
        db.close();
    }

}