package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mad_project.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.HashSet;
import java.util.Set;

public class SwapSeat extends AppCompatActivity {
    private Set<String> bookedSeats = new HashSet<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_book_seats);
        TextView txtswap = findViewById(R.id.textView);
        txtswap.setText("Swap Seat");

        // Add booked seats
        bookedSeats.add("I1");
        bookedSeats.add("I2");
        bookedSeats.add("H2");

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
                // Set normal seats to default color
                seatButton.setBackgroundTintList(getColorStateList(R.color.gray));
                seatButton.setEnabled(false);

                // Check if the seat is booked
                if (bookedSeats.contains(seatId)) {
                    seatButton.setBackgroundTintList(getColorStateList(R.color.seat_booked_color));
                    seatButton.setEnabled(true);
                    seatButton.setOnClickListener(v -> showSwapSeatDialog(seatId));
                }
            }
        }
    }

    private void showSwapSeatDialog(String seatId) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(SwapSeat.this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_swap_seat, null);
        bottomSheetDialog.setContentView(bottomSheetView);

        Button btnSwapSeat = bottomSheetView.findViewById(R.id.btnSwapSeat);
        btnSwapSeat.setOnClickListener(v -> {
            Toast.makeText(SwapSeat.this, "Swapping seat " + seatId, Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }
}