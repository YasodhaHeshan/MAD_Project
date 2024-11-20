package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.mad_project.R;

import java.util.HashSet;
import java.util.Set;

public class BookSeat extends AppCompatActivity {
    private Set<String> selectedSeats = new HashSet<>();
    private Set<String> bookedSeats = new HashSet<>();
    private TextView txtSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_book_seats);

        // Add booked seats
        bookedSeats.add("I1");
        bookedSeats.add("I2");
        bookedSeats.add("H2");

        txtSelected = findViewById(R.id.txtselected);
        Button btnBook = findViewById(R.id.doneButton);

        // List of all seat IDs
        int[] seatIds = {
                R.id.I1, R.id.I2, R.id.I3, R.id.I4, R.id.I5,
                R.id.H1, R.id.H2, R.id.H3, R.id.H4,
                R.id.G1, R.id.G2, R.id.G3, R.id.G4,
                R.id.F1, R.id.F2, R.id.F3, R.id.F4,
                R.id.E1, R.id.E2, R.id.E3, R.id.E4,
                R.id.D1, R.id.D2, R.id.D3, R.id.D4,
                R.id.C1, R.id.C2, R.id.C3, R.id.C4,
                R.id.B1, R.id.B2, R.id.B3, R.id.B4,
                R.id.A1, R.id.A2, R.id.A3, R.id.A4
        };

        // Loop through all seats and set up their properties
        for (int resId : seatIds) {
            Button seatButton = findViewById(resId);

            if (seatButton != null) {
                String seatId = getResources().getResourceEntryName(resId);
                // Check if the seat is booked
                if (bookedSeats.contains(seatId)) {
                    seatButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.seat_booked_color));
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
                                seatButton.setBackgroundTintList(ContextCompat.getColorStateList(BookSeat.this, R.color.seat_default_color));
                                selectedSeats.remove(seatId);
                                seatButton.setTextColor(ContextCompat.getColor(BookSeat.this, R.color.seat_default_color));
                            } else {
                                // Select seat
                                seatButton.setBackgroundTintList(ContextCompat.getColorStateList(BookSeat.this, R.color.seat_selected_color));
                                selectedSeats.add(seatId);
                                seatButton.setTextColor(ContextCompat.getColor(BookSeat.this, R.color.colorWhite));
                            }
                            isSelected = !isSelected;
                            updateSelectedSeatsText();
                        }
                    });
                }
            }
        }
    }

    private void updateSelectedSeatsText() {
        String selectedSeatsText = "Selected Seats: " + selectedSeats;
        txtSelected.setText(selectedSeatsText);
    }
}