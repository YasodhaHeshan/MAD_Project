package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;

import java.util.HashSet;
import java.util.Set;

public class BookSeat extends AppCompatActivity {
    private Set<String> selectedSeats = new HashSet<>();
    private TextView txtSelected;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_book_seats);

        txtSelected = findViewById(R.id.txtselected);
        // Add click listeners to all seat buttons
        addSeatClickListener(R.id.I1);
        addSeatClickListener(R.id.I2);
        addSeatClickListener(R.id.I3);
        addSeatClickListener(R.id.I4);
        addSeatClickListener(R.id.I5);
        addSeatClickListener(R.id.H1);
        addSeatClickListener(R.id.H2);
        addSeatClickListener(R.id.H3);
        addSeatClickListener(R.id.H4);
        addSeatClickListener(R.id.G1);
        addSeatClickListener(R.id.G2);
        addSeatClickListener(R.id.G3);
        addSeatClickListener(R.id.G4);
        addSeatClickListener(R.id.F1);
        addSeatClickListener(R.id.F2);
        addSeatClickListener(R.id.F3);
        addSeatClickListener(R.id.F4);
        addSeatClickListener(R.id.E1);
        addSeatClickListener(R.id.E2);
        addSeatClickListener(R.id.E3);
        addSeatClickListener(R.id.E4);
        addSeatClickListener(R.id.D1);
        addSeatClickListener(R.id.D2);
        addSeatClickListener(R.id.D3);
        addSeatClickListener(R.id.D4);
        addSeatClickListener(R.id.C1);
        addSeatClickListener(R.id.C2);
        addSeatClickListener(R.id.C3);
        addSeatClickListener(R.id.C4);
        addSeatClickListener(R.id.B1);
        addSeatClickListener(R.id.B2);
        addSeatClickListener(R.id.B3);
        addSeatClickListener(R.id.B4);
        addSeatClickListener(R.id.A1);
        addSeatClickListener(R.id.A2);
        addSeatClickListener(R.id.A3);
        addSeatClickListener(R.id.A4);

        ImageButton backbutton = findViewById(R.id.back_button);
        backbutton.setOnClickListener(v -> {
            finish();
        });
    }

    private void addSeatClickListener(int buttonId) {
        Button seatButton = findViewById(buttonId);
        String seatId = getResources().getResourceEntryName(buttonId);

        seatButton.setOnClickListener(new View.OnClickListener() {
            private boolean isSelected = true;

            @Override
            public void onClick(View v) {
                if (isSelected) {
                    seatButton.setBackgroundTintList(getColorStateList(R.color.seat_selected_color));
                    selectedSeats.add(seatId);
                    seatButton.setTextColor(getResources().getColor(R.color.colorWhite));
                } else {
                    seatButton.setBackgroundTintList(getColorStateList(R.color.seat_default_color));
                    selectedSeats.remove(seatId);
                    seatButton.setTextColor(getResources().getColor(R.color.seat_default_color));
                }
                isSelected = !isSelected;
                updateSelectedSeatsText();
            }
        });

    }
    private void updateSelectedSeatsText() {
        txtSelected.setText("Selected Seats: " + String.join(", ", selectedSeats));
    }
}
