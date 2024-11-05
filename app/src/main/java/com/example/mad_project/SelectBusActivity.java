package com.example.mad_project;

import android.os.Bundle;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.MaterialDatePicker;

public class SelectBusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bus);

        EditText dateField = findViewById(R.id.dateField);
        dateField.setOnClickListener(v -> showDatePicker(dateField));
    }

    private void showDatePicker(EditText dateField) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .build();

        datePicker.addOnPositiveButtonClickListener(selection -> {
            dateField.setText(datePicker.getHeaderText());
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
    }
}