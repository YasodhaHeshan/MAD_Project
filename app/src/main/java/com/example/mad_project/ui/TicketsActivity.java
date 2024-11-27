package com.example.mad_project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mad_project.R;
import com.example.mad_project.MainActivity;
import com.example.mad_project.adapter.TicketAdapter;
import com.example.mad_project.data.AppDatabase;
import com.example.mad_project.data.PaymentDao;
import com.example.mad_project.data.Ticket;
import com.example.mad_project.data.TicketDao;
import com.example.mad_project.data.Bus;
import com.example.mad_project.data.Payment;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.text.NumberFormat;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.graphics.drawable.GradientDrawable;
import androidx.core.content.ContextCompat;

public class TicketsActivity extends MainActivity {
    private RecyclerView ticketsRecyclerView;
    private TextView emptyStateText;
    private AppDatabase db;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "LK"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_tickets, contentFrame);
        setupNavigation(true, true, "My Tickets");

        // Initialize database
        db = AppDatabase.getDatabase(this);
        initializeViews();
        loadTickets();
    }

    private void initializeViews() {
        ticketsRecyclerView = findViewById(R.id.ticketsRecyclerView);
        emptyStateText = findViewById(R.id.emptyStateText);
        
        ticketsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadTickets() {
        executor.execute(() -> {
            TicketDao ticketDao = db.ticketDao();
            PaymentDao paymentDao = db.paymentDao();
            List<Ticket> tickets = ticketDao.getAllTickets();

            runOnUiThread(() -> {
                if (tickets.isEmpty()) {
                    showEmptyState();
                } else {
                    displayTickets(tickets, paymentDao);
                }
            });
        });
    }

    private void showEmptyState() {
        ticketsRecyclerView.setVisibility(View.GONE);
        emptyStateText.setVisibility(View.VISIBLE);
    }

    private void displayTickets(List<Ticket> tickets, PaymentDao paymentDao) {
        ticketsRecyclerView.setVisibility(View.VISIBLE);
        emptyStateText.setVisibility(View.GONE);
        
        TicketAdapter adapter = new TicketAdapter(tickets, this::showTicketDetails, paymentDao);
        ticketsRecyclerView.setAdapter(adapter);
    }

    private void showTicketDetails(Ticket ticket) {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(this);
        View bottomSheetView = getLayoutInflater().inflate(R.layout.ticket_details_sheet, null);
        
        // Find views in the bottom sheet
        TextView ticketNumberText = bottomSheetView.findViewById(R.id.ticketNumberText);
        TextView startLocationText = bottomSheetView.findViewById(R.id.startLocationText);
        TextView endLocationText = bottomSheetView.findViewById(R.id.endLocationText);
        TextView journeyDateText = bottomSheetView.findViewById(R.id.journeyDateText);
        TextView seatNumberText = bottomSheetView.findViewById(R.id.seatNumberText);
        TextView busDetailsText = bottomSheetView.findViewById(R.id.busDetailsText);
        Button swapSeatButton = bottomSheetView.findViewById(R.id.swapSeatButton);
        
        // Format date
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        String formattedDate = dateFormat.format(new Date(ticket.getJourneyDate()));
        
        // Set ticket details
        ticketNumberText.setText("Ticket #" + ticket.getId());
        startLocationText.setText(ticket.getSource());
        endLocationText.setText(ticket.getDestination());
        journeyDateText.setText(formattedDate);
        seatNumberText.setText("Seat: " + ticket.getSeatNumber());
        
        // Load bus details and payment info asynchronously
        executor.execute(() -> {
            Bus bus = db.busDao().getBusById(ticket.getBusId());
            Payment payment = ticket.getPaymentId() != null ? 
                db.paymentDao().getPaymentById(ticket.getPaymentId()) : null;
            
            runOnUiThread(() -> {
                // Set bus details
                if (bus != null) {
                    busDetailsText.setText(String.format("%s (%s)", 
                        bus.getRegistrationNumber(),
                        bus.getModel()));
                }
            });
        });
        
        // Handle swap seat button
        swapSeatButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, SeatSelectionActivity.class);
            intent.putExtra("bus_id", ticket.getBusId());
            intent.putExtra("is_swap", true);
            intent.putExtra("current_seat", ticket.getSeatNumber());
            intent.putExtra("ticket_id", ticket.getId());
            startActivity(intent);
            bottomSheet.dismiss();
        });
        
        bottomSheet.setContentView(bottomSheetView);
        bottomSheet.show();
    }
}
