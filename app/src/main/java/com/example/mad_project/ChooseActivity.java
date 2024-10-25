package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class ChooseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);

        Button customerreg = findViewById(R.id.btncustomer);
        Button ownerreg = findViewById(R.id.btnowner);
        Button driverreg = findViewById(R.id.btndriver);

        customerreg.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        ownerreg.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this,RegisterActivity.class);
            startActivity(intent);
        });

        driverreg.setOnClickListener(v -> {
            Intent intent = new Intent(ChooseActivity.this,RegisterActivity.class);
            startActivity(intent);
        });
    }




}
