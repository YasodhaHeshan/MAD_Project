package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;

import java.util.HashSet;
import java.util.Set;

public class BookSeat extends AppCompatActivity {
    private Set<String> selectedSeats = new HashSet<>();
    private TextView txtSelected;
    private Set<String> bookedSeats = new HashSet<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_book_seats);

        // Add booked seats
        bookedSeats.add("I1");
        bookedSeats.add("I2");
        bookedSeats.add("H2");

        txtSelected = findViewById(R.id.txtselected);

        // List of all seat IDs
        String[] seatIds = {
                "I1", "I2", "I3", "I4", "I5",
                "H1", "H2", "H3", "H4",
                "G1", "G2", "G3", "G4",
                "F1", "F2", "F3", "F4",
                "E1", "E2", "E3", "E4",
                "D1", "D2", "D3", "D4",
                "C1", "C2", "C3", "C4",
                "B1", "B2", "B3", "B4",
                "A1", "A2", "A3", "A4"
        };

        // Loop through all seats and set up their properties
        for (String seatId : seatIds) {
            int resId = getResources().getIdentifier(seatId, "id", getPackageName());
            Button seatButton = findViewById(resId);

            if (seatButton != null) {
                // Check if the seat is booked
                if (bookedSeats.contains(seatId)) {
                    seatButton.setBackgroundTintList(getColorStateList(R.color.seat_booked_color));
                    seatButton.setOnClickListener(v ->
                            Toast.makeText(BookSeat.this, "Seat " + seatId + " is already booked", Toast.LENGTH_SHORT).show()
                    );
                    //seatButton.setEnabled(false); // Disable booked seats
                } else {
                    // Set up listener for available seats
                    seatButton.setOnClickListener(new View.OnClickListener() {
                        private boolean isSelected = false;

                        @Override
                        public void onClick(View v) {
                            if (isSelected) {
                                // Deselect seat
                                seatButton.setBackgroundTintList(getColorStateList(R.color.seat_default_color));
                                selectedSeats.remove(seatId);
                                seatButton.setTextColor(getResources().getColor(R.color.seat_default_color));
                            } else {
                                // Select seat
                                seatButton.setBackgroundTintList(getColorStateList(R.color.seat_selected_color));
                                selectedSeats.add(seatId);
                                seatButton.setTextColor(getResources().getColor(R.color.colorWhite));
                            }
                            isSelected = !isSelected;
                            updateSelectedSeatsText();
                        }
                    });
                }
            }
        }

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());
    }

    private void updateSelectedSeatsText() {
        txtSelected.setText("Selected Seats: " + String.join(", ", selectedSeats));
    }
}
