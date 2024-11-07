package com.example.mad_project;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        EditText from = findViewById(R.id.fromField);
        EditText to = findViewById(R.id.toField);
        Button searchButton = findViewById(R.id.btnSearch);

        searchButton.setOnClickListener(v -> {
            String origin = from.getText().toString();
            String destination = to.getText().toString();

            Toast.makeText(this, "Searching for buses from " + origin + " to " + destination, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, MapActivity.class);
            intent.putExtra("origin", origin);
            intent.putExtra("destination", destination);
            startActivity(intent);
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });


//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_main);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layoutmain), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//           Button registerbtn = findViewById(R.id.btnStart);
//
//        registerbtn.setOnClickListener(v -> {
//            Intent intent = new Intent(MainActivity.this, MapActivity.class);
//            startActivity(intent);
//        });
    }
}