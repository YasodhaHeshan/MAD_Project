package com.example.mad_project.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
                // Set normal seats to default color
                seatButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.gray));
                seatButton.setEnabled(false);

                // Check if the seat is booked
                if (bookedSeats.contains(seatId)) {
                    seatButton.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.seat_booked_color));
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